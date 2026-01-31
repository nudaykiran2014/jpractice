package com.example.s3upload.service;

import com.example.s3upload.dto.FileUploadResponse;
import com.example.s3upload.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${file.upload.allowed-extensions}")
    private String allowedExtensions;

    @Value("${file.upload.max-size-mb}")
    private int maxSizeMb;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public FileUploadResponse uploadFile(MultipartFile file) {
        validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String s3Key = generateS3Key(originalFileName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String s3Url = getS3Url(s3Key);

            return FileUploadResponse.builder()
                    .fileName(originalFileName)
                    .s3Key(s3Key)
                    .s3Url(s3Url)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .message("File uploaded successfully")
                    .build();

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file: " + e.getMessage());
        } catch (S3Exception e) {
            throw new FileUploadException("S3 error: " + e.awsErrorDetails().errorMessage());
        }
    }

    public FileUploadResponse uploadFile(MultipartFile file, String folder) {
        validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String s3Key = generateS3Key(folder, originalFileName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String s3Url = getS3Url(s3Key);

            return FileUploadResponse.builder()
                    .fileName(originalFileName)
                    .s3Key(s3Key)
                    .s3Url(s3Url)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .message("File uploaded successfully")
                    .build();

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file: " + e.getMessage());
        } catch (S3Exception e) {
            throw new FileUploadException("S3 error: " + e.awsErrorDetails().errorMessage());
        }
    }

    public List<FileUploadResponse> uploadMultipleFiles(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .toList();
    }

    public void deleteFile(String s3Key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.deleteObject(deleteRequest);
        } catch (S3Exception e) {
            throw new FileUploadException("Failed to delete file: " + e.awsErrorDetails().errorMessage());
        }
    }

    public String generatePresignedUrl(String s3Key) {
        return getS3Url(s3Key);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is empty or null");
        }

        long maxSizeBytes = (long) maxSizeMb * 1024 * 1024;
        if (file.getSize() > maxSizeBytes) {
            throw new FileUploadException(
                    String.format("File size exceeds maximum allowed size of %d MB", maxSizeMb));
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new FileUploadException("Invalid file name");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedList = Arrays.asList(allowedExtensions.split(","));
        
        if (!allowedList.contains(extension)) {
            throw new FileUploadException(
                    String.format("File extension '%s' is not allowed. Allowed: %s", 
                            extension, allowedExtensions));
        }
    }

    private String generateS3Key(String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String sanitizedFileName = sanitizeFileName(originalFileName);
        
        return String.format("uploads/%s/%s-%s", timestamp, uniqueId, sanitizedFileName);
    }

    private String generateS3Key(String folder, String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String sanitizedFileName = sanitizeFileName(originalFileName);
        
        return String.format("%s/%s/%s-%s", folder, timestamp, uniqueId, sanitizedFileName);
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String getS3Url(String s3Key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);
    }
}
