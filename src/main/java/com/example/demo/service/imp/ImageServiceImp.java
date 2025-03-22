package com.example.demo.service.imp;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.ImageService;
import com.example.demo.service.S3Service;

import jakarta.transaction.Transactional;

@Service
public class ImageServiceImp implements ImageService {

	private ImageRepository imageRepository;
	private S3Service s3Service;

	public ImageServiceImp(ImageRepository imageRepository, S3Service s3Service) {
		this.imageRepository = imageRepository;
		this.s3Service = s3Service;
	}

	@Override
	@Transactional
	public void deleteImage(String imageId) throws Exception {
		Image image = imageRepository.findById(imageId)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found"));
		s3Service.deleteFile(image.getImageTitle());
		imageRepository.delete(image);
	}

}
