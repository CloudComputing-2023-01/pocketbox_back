package store.pocketbox.app.service;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Component
public interface S3Component {

    S3Presigner getS3Presigner();

    S3Client getS3Client();

    final public String bucketName = "cloudspringtestcloudspringtest";
}
