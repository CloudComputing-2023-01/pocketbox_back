package store.pocketbox.app.service.impl;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import store.pocketbox.app.service.S3Component;


@Component
public class S3ComponentImpl implements S3Component {
    private final S3Presigner signer = S3Presigner.builder().region(Region.US_EAST_1).build();
    private final S3Client client = S3Client.builder().region(Region.US_EAST_1).build();
    private final S3AsyncClient asyncClient = S3AsyncClient.builder().region(Region.US_EAST_1).build();

    public S3Presigner getS3Presigner() {
        return signer;
    }

    public S3Client getS3Client() {
        return client;
    }

    public S3AsyncClient getS3AsyncClient() {
        return asyncClient;
    }
}
