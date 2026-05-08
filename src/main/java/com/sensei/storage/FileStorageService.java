package com.sensei.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    private S3Presigner presigner;

    @PostConstruct
    public void init() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    /**
     * Generates a pre-signed URL for uploading a file to S3.
     * Frontend uses this URL to upload directly to S3 (no file passes through our server).
     *
     * @param folder  e.g., "photos" or "certificates"
     * @param originalFileName  original file name from client
     * @return UploadUrlResponse with the pre-signed URL and the S3 object key
     */
    public FileStorageDto.UploadUrlResponse generateUploadUrl(String folder, String originalFileName) {
        // Generate unique key to avoid collisions
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String objectKey = "teachers/" + folder + "/" + UUID.randomUUID() + extension;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putRequest)
                .signatureDuration(Duration.ofMinutes(10)) // URL valid for 10 minutes
                .build();

        String uploadUrl = presigner.presignPutObject(presignRequest).url().toString();

        FileStorageDto.UploadUrlResponse response = new FileStorageDto.UploadUrlResponse();
        response.setUploadUrl(uploadUrl);
        response.setObjectKey(objectKey);
        response.setExpiresInMinutes(10);
        return response;
    }

    /**
     * Generates a pre-signed URL for downloading/viewing a file from S3.
     * Used to display teacher photos and certificates.
     *
     * @param objectKey  the S3 object key stored in the database
     * @return pre-signed download URL (valid for 1 hour)
     */
    public String generateDownloadUrl(String objectKey) {
        if (objectKey == null || objectKey.isEmpty()) return null;

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getRequest)
                .signatureDuration(Duration.ofHours(1)) // URL valid for 1 hour
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }
}
