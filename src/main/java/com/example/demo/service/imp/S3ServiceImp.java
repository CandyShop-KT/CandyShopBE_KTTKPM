package com.example.demo.service.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.S3Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3ServiceImp implements S3Service {

    private final S3Client s3Client;
    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImp.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private int attemptCount = 0;

    private final ApplicationContext context;

    public S3ServiceImp(S3Client s3Client, ApplicationContext context) {
        this.s3Client = s3Client;
        this.context = context;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile);

        // Gọi lại chính mình thông qua proxy để @Retryable hoạt động
        S3ServiceImp proxy = context.getBean(S3ServiceImp.class);
        String fileUrl = proxy.uploadFileTos3bucket(fileName, file);

        file.delete(); // xóa file tạm sau khi upload
        return fileUrl;
    }

    @Retryable(
        value = { IOException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    public String uploadFileTos3bucket(String fileName, File file) throws IOException {
        attemptCount++;
        logger.info("Lần thử thứ #{} để tải tệp lên S3", attemptCount);

        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build(),
                RequestBody.fromFile(file)
            );
            logger.info("Tải tệp '{}' lên S3 bucket '{}' thành công", fileName, bucketName);
            return fileName;
        } catch (S3Exception e) {
            logger.error("S3Exception khi tải lên S3: {}", e.awsErrorDetails().errorMessage());
            throw new IOException("Lỗi từ S3", e);
        } catch (Exception e) {
            logger.error("Ngoại lệ không xác định khi tải lên: {}", e.getMessage());
            throw new IOException("Lỗi không xác định khi tải lên S3", e);
        }
    }

    @Recover
    public String recover(IOException e, String fileName, File file) {
        logger.error("Tải lên tệp thất bại sau khi thử lại nhiều lần: {}.File:{}, Lỗi: {}",fileName, e.getMessage());
       return null;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    @Override
    public void deleteFile(String key) throws Exception {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
