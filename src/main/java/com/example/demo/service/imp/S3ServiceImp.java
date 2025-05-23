package com.example.demo.service.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.management.RuntimeErrorException;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.CustomException;
import com.example.demo.service.S3Service;

import software.amazon.awssdk.core.exception.SdkClientException;
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

    private final ApplicationContext context;

    public S3ServiceImp(S3Client s3Client, ApplicationContext context) {
        this.s3Client = s3Client;
        this.context = context;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile);

        // Gọi qua Spring Proxy (đảm bảo proxy có thể thực hiện retry)
        S3Service proxy = context.getBean(S3Service.class); // Lấy Spring Bean để sử dụng retry
        String fileUrl = proxy.uploadFileTos3bucket(fileName, file); // Gọi phương thức qua proxy Spring

        file.delete(); // Xóa file tạm sau khi upload
        return fileUrl;
    }

    @Retryable(
        value = { IOException.class, SdkClientException.class, UnknownHostException.class },
        maxAttempts = 5,
        backoff = @Backoff(delay = 3000)
    )
    public String uploadFileTos3bucket(String fileName, File file) throws IOException {
        logger.info("Đang tải lên tệp '{}' lên S3 bucket '{}'", fileName, bucketName);

        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build(),
                RequestBody.fromFile(file)
            );
            logger.info("Tải tệp '{}' lên S3 thành công", fileName);
            return fileName;
        } catch (S3Exception e) {
            logger.error("Lỗi khi tải lên S3: {}", e.awsErrorDetails().errorMessage());
            throw new IOException("Lỗi từ S3", e);
        } catch (Exception e) {
            logger.error("Lỗi khi tải lên: {}", e.getMessage());
            if (e.getCause() instanceof UnknownHostException) {
                throw new UnknownHostException(e.getCause().getMessage());
            }
            if (e instanceof SdkClientException && e.getCause() instanceof UnknownHostException) {
                throw (SdkClientException) e;
            }
            throw new IOException("Lỗi không xác định khi tải lên S3", e);
        }
    }

    @Recover
    public String recover(IOException e, String fileName, File file) {
        logger.error("Không thể tải lên tệp sau khi thử lại nhiều lần: {}. Lỗi: {}", fileName, e.getMessage());
        throw new CustomException("Tải ảnh lên thất bại. Vui lòng kiểm tra kết nối hoặc thử lại sau.");
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
