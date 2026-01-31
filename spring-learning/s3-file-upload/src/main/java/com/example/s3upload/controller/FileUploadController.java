package com.example.s3upload.controller;

import com.example.s3upload.dto.FileUploadResponse;
import com.example.s3upload.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final S3Service s3Service;

    public FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Upload a single file to S3
     * 
     * POST /api/files/upload
     * Content-Type: multipart/form-data
     * Body: file (MultipartFile)
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {
        
        FileUploadResponse response = s3Service.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Upload a single file to a specific folder in S3
     * 
     * POST /api/files/upload/{folder}
     * Content-Type: multipart/form-data
     * Body: file (MultipartFile)
     */
    @PostMapping(value = "/upload/{folder}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFileToFolder(
            @RequestParam("file") MultipartFile file,
            @PathVariable String folder) {
        
        FileUploadResponse response = s3Service.uploadFile(file, folder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Upload multiple files to S3
     * 
     * POST /api/files/upload-multiple
     * Content-Type: multipart/form-data
     * Body: files (MultipartFile[])
     */
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FileUploadResponse>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files) {
        
        List<FileUploadResponse> responses = s3Service.uploadMultipleFiles(files);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    /**
     * Delete a file from S3
     * 
     * DELETE /api/files?key=uploads/2024/01/20/abc123-file.jpg
     */
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam("key") String s3Key) {
        s3Service.deleteFile(s3Key);
        return ResponseEntity.ok(Map.of(
                "message", "File deleted successfully",
                "key", s3Key
        ));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
