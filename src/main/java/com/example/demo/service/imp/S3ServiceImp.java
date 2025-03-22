package com.example.demo.service.imp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
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

	private S3Client s3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public S3ServiceImp(S3Client s3Client) {
		this.s3Client = s3Client;
	}
	
	@Override
	public String uploadFile(MultipartFile multipartFile) throws IOException {
		String fileUrl = "";
		File file = convertMultiPartToFile(multipartFile);
		String fileName = generateFileName(multipartFile);
		fileUrl = uploadFileTos3bucket(fileName, file);
		file.delete();
		return fileUrl;
	}

	private String uploadFileTos3bucket(String fileName, File file) {
		s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).build(),
				RequestBody.fromFile(file));
		return fileName;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fileOutputStream = new FileOutputStream(convFile);
		fileOutputStream.write(file.getBytes());
		fileOutputStream.close();
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
