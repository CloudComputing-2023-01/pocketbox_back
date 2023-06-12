package store.pocketbox.app.web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.pocketbox.app.converter.S3Converter;
import store.pocketbox.app.exception.StatusCode;
import store.pocketbox.app.service.S3Service;
import store.pocketbox.app.web.dto.FileRequestDto;
import store.pocketbox.app.web.dto.FolderRequestDto;
import store.pocketbox.app.web.dto.base.DefaultRes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    @Autowired
    private final S3Service s3Service;

    @GetMapping("/folder")
    public ResponseEntity getChildFolder(@RequestBody FolderRequestDto.GetFolderChildRequest request) {
        try{
            var list = s3Service.listFolder(S3Converter.toPath(request.getName(), request.getPath()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "", S3Converter.toGetFolderChildResponse(list.directories, list.files)), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/folder")
    public ResponseEntity createFolder(@RequestBody FolderRequestDto.CreateFolderRequest request) {
        try {
            s3Service.createFolder(S3Converter.toPath(request.getName(), request.getPath()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ""), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/folder")
    public ResponseEntity deleteFolder(@RequestBody FolderRequestDto.DeleteFolderRequest request) {
        try {
            s3Service.deleteFolder(S3Converter.toPath(request.getName(), request.getPath()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ""), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/file")
    public ResponseEntity createFile(@RequestBody FileRequestDto.CreateFileRequest request) {
        try {
            var res = s3Service.createPreSignedForUploadFile(S3Converter.toFilePath(request.getName(), request.getPath(), request.getFilename()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "", S3Converter.toCreateFileResponse(res)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file")
    public ResponseEntity getFile(@RequestBody FileRequestDto.GetFileRequest request) {
        try {
            var res = s3Service.createPreSignedForDownloadFile(S3Converter.toFilePath(request.getName(), request.getPath(), request.getFilename()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, "", S3Converter.toGetFileResponse(res)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestBody FileRequestDto.DeleteFileRequest request) {
        try {
            s3Service.deleteFile(S3Converter.toFilePath(request.getName(), request.getPath(), request.getFilename()));
            return new ResponseEntity(DefaultRes.res(StatusCode.OK, ""), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/folder/download/zip/**", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadZip(HttpServletRequest request) throws IOException, InterruptedException {

        // (1)
        String prefix = getPrefix(request.getRequestURI(), "/folder/download/zip/");

        // (2)
        Resource resource = s3Service.downloadZip(prefix);

        // (3)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + new String(resource.getFilename().getBytes("UTF-8"), "ISO-8859-1"));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private String getPrefix(String uri, String regex) {
        String[] split = uri.split(regex);
        return split.length < 2 ? "" : split[1];
    }
}
