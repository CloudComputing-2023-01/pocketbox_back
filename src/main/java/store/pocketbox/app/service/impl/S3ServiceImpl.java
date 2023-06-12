package store.pocketbox.app.service.impl;

import com.amazonaws.services.s3.transfer.*;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import store.pocketbox.app.service.S3Component;
import store.pocketbox.app.service.S3Service;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.apache.catalina.security.SecurityUtil.remove;

@Component
@Slf4j
public class S3ServiceImpl implements S3Service {

    @Autowired
    S3Component s3Component;
    @Autowired
    TransferManager transferManager;

    @Override
    public String createPreSignedForUploadFile(FilePath path) {
        if(!isFolderExists(path.getParentFolder())) {
            throw new UnsupportedOperationException("Please create destination folder first before uploading file");
        }

        PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(s3Component.bucketName).key(path.canonicalPath)
                //.contentType("text/plain")
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(10)).putObjectRequest(objectRequest).build();
        PresignedPutObjectRequest presignedRequest = s3Component.getS3Presigner().presignPutObject(presignRequest);
        String myURL = presignedRequest.url().toString();

        return myURL;
    }

    @Override
    public String createPreSignedForDownloadFile(FilePath path) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(s3Component.bucketName).key(path.canonicalPath).build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(60)).getObjectRequest(getObjectRequest).build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Component.getS3Presigner().presignGetObject(getObjectPresignRequest);
        String theUrl = presignedGetObjectRequest.url().toString();

        return theUrl;
    }

    @Override
    public void deleteFile(FilePath path) {
        if(!isFolderExists(path.getParentFolder())) {
            throw new UnsupportedOperationException("");
        }

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(s3Component.bucketName).key(path.canonicalPath).build();

        s3Component.getS3Client().deleteObject(deleteObjectRequest);
    }

    @Override
    public void createFolder(FolderPath path) {
        guaranteeFolderExists(path);
    }

    @Override
    public ListResult listFolder(FolderPath path) {
        var resp = runListRequest(path);

        var directories = listSubDirectoriesNames(resp);
        var files = listFilesNames(resp);

        return new ListResult(directories, files);
    }

    @Override
    public void deleteFolder(FolderPath path) {
        deleteFolderRecursive(path, 0);
    }

    private void deleteFolderRecursive(FolderPath path, int depth) {
        if (depth > 5) {
            return;
        }

        var resp = runListRequest(path);
        var directories = listSubDirectoriesNames(resp);
        var files = listFilesNames(resp);

        files.forEach(this::deleteFile);
        directories.forEach((x) -> deleteFolderRecursive(x, depth + 1));
    }

    private void runCreateFolderRequest(FolderPath path) {
        String name = path.canonicalPath + ".folder";
        PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(s3Component.bucketName).key(name).contentType("text/plain").build();

        s3Component.getS3Client().putObject(objectRequest, RequestBody.fromBytes(new byte[]{0x01}));
    }

    private ListObjectsV2Response runListRequest(FolderPath path) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder().bucket(s3Component.bucketName).prefix(path.canonicalPath).delimiter("/").build();

        ListObjectsV2Response resp = s3Component.getS3Client().listObjectsV2(listRequest);

        return resp;
    }

    private List<FolderPath> listSubDirectoriesNames(ListObjectsV2Response resp) {
        return resp.commonPrefixes().stream().map((x) -> new FolderPath(Arrays.stream(x.prefix().split("/")).toList())).toList();
    }

    private List<FilePath> listFilesNames(ListObjectsV2Response resp) {
        return resp.contents().stream().map((x) -> new FilePath(Arrays.stream(x.key().split("/")).toList())).toList();
    }

    private void guaranteeFolderExists(FolderPath path) {
        for (int i = 1; i < path.pathElements.size(); i++) {
            var resp = runListRequest(new FolderPath(path.pathElements.subList(0, i)));

            var directories = listSubDirectoriesNames(resp);
            int finalI = i;
            if(finalI == path.pathElements.size() || !directories.stream().anyMatch((x) -> x.pathElements.get(finalI).equals(path.pathElements.get(finalI)))) {
                runCreateFolderRequest(new FolderPath(path.pathElements.subList(0, i + 1)));
            }
        }
    }

    private Boolean isFolderExists(FolderPath path) {
        for (int i = 1; i < path.pathElements.size() - 1; i++) {
            var resp = runListRequest(new FolderPath(path.pathElements.subList(0, i)));

            var directories = listSubDirectoriesNames(resp);
            int finalI = i;
            if(!directories.stream().anyMatch((x) -> x.pathElements.get(finalI + 1).equals(path.pathElements.get(finalI + 1)))) {
                return false;
            }
        }

        return true;
    }
    public Resource downloadZip(String prefix) throws IOException, InterruptedException {
        String bucket = s3Component.bucketName;

        File localDirectory = new File(RandomStringUtils.randomAlphanumeric(6) + "-s3-download");
        // 서버 로컬에 생성되는 zip 파일
        ZipFile zipFile = new ZipFile(RandomStringUtils.randomAlphanumeric(6) + "-s3-download.zip");
        log.info(transferManager.downloadDirectory(bucket, prefix, localDirectory).getBucketName());
        log.info(String.valueOf(transferManager.downloadDirectory(bucket, prefix, localDirectory).getState()));
        log.info(transferManager.downloadDirectory(bucket, prefix, localDirectory).getKeyPrefix());
        try {
            // (2)
            // TransferManager -> localDirectory에 파일 다운로드
            MultipleFileDownload downloadDirectory = transferManager.downloadDirectory(bucket, prefix, localDirectory);

            // (3)
            // 다운로드 상태 확인
            log.info("[" + prefix + "] download progressing... start");
            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
            while (!downloadDirectory.isDone()) {
                Thread.sleep(1000);
                TransferProgress progress = downloadDirectory.getProgress();
                double percentTransferred = progress.getPercentTransferred();
                log.info("[" + prefix + "] " + decimalFormat.format(percentTransferred) + "% download progressing...");
            }
            log.info("[" + prefix + "] download directory from S3 success!");

            // (4)
            // 로컬 디렉토리 -> 로컬 zip 파일에 압축
            log.info("compressing to zip file...");
            zipFile.addFolder(new File(localDirectory.getName() + "/" + prefix));
        } finally {
            // (5)
            // 로컬 디렉토리 삭제
            File[] files = localDirectory.listFiles();
            assert files != null;
            for (File file : files) {
                remove(file);
            }
            if (localDirectory.delete()) {
                log.info("File [" + localDirectory.getName() + "] delete success");
            }
        }

        // (6)
        // 파일 Resource 리턴
        return new FileSystemResource(zipFile.getFile().getName());
    }
}
