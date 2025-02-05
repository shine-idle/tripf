package com.shineidle.tripf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 클라이언트를 설정하는 구성 클래스
 */
@Configuration
public class S3Config {
    /**
     * AWS 액세스 키
     */
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    /**
     * AWS 시크릿 키
     */
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    /**
     * AWS S3 서비스가 위치한 리전 정보
     */
    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * AWS S3 클라이언트를 생성하는 빈
     *
     * @return S3Client 객체
     */
    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
