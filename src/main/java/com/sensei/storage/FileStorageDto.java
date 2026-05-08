package com.sensei.storage;

import lombok.Data;

public class FileStorageDto {

    @Data
    public static class UploadUrlRequest {
        private String folder;    // "photos" or "certificates"
        private String fileName;  // original file name
    }

    @Data
    public static class UploadUrlResponse {
        private String uploadUrl;       // pre-signed PUT URL
        private String objectKey;       // S3 key to store in DB
        private int expiresInMinutes;
    }

    @Data
    public static class SaveFileKeyRequest {
        private String objectKey;
        private String type; // "photo" or "certificate"
    }
}
