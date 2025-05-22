package com.example.demo.service;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	
	 public String uploadFile(MultipartFile multipartFile) throws IOException;
	
	public void deleteFile(String filePath) throws Exception;
	
	 public String uploadFileTos3bucket(String fileName, File file) throws IOException;
}
