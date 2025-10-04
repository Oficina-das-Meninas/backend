package br.org.oficinadasmeninas.infra.objectstorage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
public class MinIoConfiguration {
    @Bean
    public S3Client s3Client(@Value("${storage.s3.endpoint}") String endpoint,
                             @Value("${storage.s3.access-key}") String accessKey,
                             @Value("${storage.s3.secret-key}") String secretKey,
                             @Value("${storage.s3.region}") String region,
                             @Value("${storage.s3.path-style-access:true}") boolean pathStyleAccess) {

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials));

        if (endpoint != null && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        if (pathStyleAccess) {
            builder.forcePathStyle(true);
        }

        return builder.build();
    }
}
