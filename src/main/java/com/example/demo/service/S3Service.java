package com.example.demo.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	
	public String uploadFile(MultipartFile multipartFile) throws IOException, Exception;
	
	public void deleteFile(String filePath) throws Exception;
	
}
