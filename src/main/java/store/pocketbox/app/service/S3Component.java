package store.pocketbox.app.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Component
public interface S3Component {
    S3Presigner getS3Presigner();
    S3Client getS3Client();
    S3AsyncClient getS3AsyncClient();
    final public String bucketName = "s3testdmstmddn";
}
