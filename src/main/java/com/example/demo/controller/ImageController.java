package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponseNoDataDTO;
import com.example.demo.service.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
	private ImageService imageService;
	
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{imageId}")
	public ResponseEntity<?> deleteImage(@PathVariable String imageId) throws Exception {
		imageService.deleteImage(imageId);
		ApiResponseNoDataDTO response = new ApiResponseNoDataDTO("Image deleted successfully", HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
