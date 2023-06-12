package store.pocketbox.app.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

import static com.amazonaws.services.s3.internal.Constants.MB;


@Configuration
public class S3Config {
    @Autowired
    S3Component s3Component;

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 s3Builder = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
        return s3Builder;
    }
    @Bean
    public TransferManager transferManager() {
        TransferManager transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3())
                .build();
        return transferManager;
    }
}