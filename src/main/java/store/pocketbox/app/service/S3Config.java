package store.pocketbox.app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import static com.amazonaws.services.s3.internal.Constants.MB;


@Configuration
public class S3Config {
    @Autowired
    S3Component s3Component;

    @Bean
    public S3TransferManager transferManager() {
        return S3TransferManager.builder()
                .s3Client(s3Component.getS3AsyncClient())
                .build();
    }
}