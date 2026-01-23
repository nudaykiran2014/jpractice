# S3 File Upload Service

Spring Boot application for uploading files to AWS S3.

## Project Structure

```
s3-file-upload/
├── pom.xml
├── src/main/java/com/example/s3upload/
│   ├── S3FileUploadApplication.java      # Main application
│   ├── config/
│   │   └── S3Config.java                 # AWS S3 client configuration
│   ├── controller/
│   │   └── FileUploadController.java     # REST endpoints
│   ├── service/
│   │   └── S3Service.java                # S3 operations
│   ├── dto/
│   │   ├── FileUploadResponse.java       # Upload response DTO
│   │   └── ErrorResponse.java            # Error response DTO
│   └── exception/
│       ├── FileUploadException.java      # Custom exception
│       └── GlobalExceptionHandler.java   # Global error handler
└── src/main/resources/
    └── application.yml                   # Configuration
```

## Prerequisites2

1. **Java 17+**
2. **Maven 3.6+**
3. **AWS Account** with S3 bucket created
4. **AWS Credentials** configured

## Configuration

### Option 1: Environment Variables (Recommended)
```bash
export AWS_S3_BUCKET=your-bucket-name
export AWS_REGION=us-east-1
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
```

### Option 2: application.yml
Update `src/main/resources/application.yml` with your values.

### Option 3: AWS CLI Profile
If you have AWS CLI configured, the app will use default credentials.

## Running the Application

```bash
cd s3-file-upload
mvn spring-boot:run
```

## API Endpoints

### 1. Upload Single File
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/your/file.jpg"
```

**Response:**
```json
{
  "fileName": "file.jpg",
  "s3Key": "uploads/2024/01/20/abc123-file.jpg",
  "s3Url": "https://bucket.s3.region.amazonaws.com/uploads/2024/01/20/abc123-file.jpg",
  "fileSize": 102400,
  "contentType": "image/jpeg",
  "message": "File uploaded successfully"
}
```

### 2. Upload to Specific Folder
```bash
curl -X POST http://localhost:8080/api/files/upload/images \
  -F "file=@/path/to/your/file.jpg"
```

### 3. Upload Multiple Files
```bash
curl -X POST http://localhost:8080/api/files/upload-multiple \
  -F "files=@/path/to/file1.jpg" \
  -F "files=@/path/to/file2.png"
```

### 4. Delete File
```bash
curl -X DELETE "http://localhost:8080/api/files?key=uploads/2024/01/20/abc123-file.jpg"
```

### 5. Health Check
```bash
curl http://localhost:8080/api/files/health
```

## File Validation

- **Allowed extensions:** jpg, jpeg, png, gif, pdf, doc, docx, txt, csv, xlsx
- **Max file size:** 50MB

## Error Responses

```json
{
  "status": 400,
  "error": "File Upload Error",
  "message": "File extension 'exe' is not allowed",
  "timestamp": "2024-01-20T10:30:00"
}
```

## AWS S3 Bucket Policy (if needed)

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:PutObject",
        "s3:GetObject",
        "s3:DeleteObject"
      ],
      "Resource": "arn:aws:s3:::your-bucket-name/*"
    }
  ]
}
```

## Testing with Postman

1. Create new POST request to `http://localhost:8080/api/files/upload`
2. Go to **Body** tab → Select **form-data**
3. Add key `file` → Change type to **File** → Select file
4. Send request
