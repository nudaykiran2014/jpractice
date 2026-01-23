package com.example.s3upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private String fileName;
    private String s3Key;
    private String s3Url;
    private long fileSize;
    private String contentType;
    private String message;
}
