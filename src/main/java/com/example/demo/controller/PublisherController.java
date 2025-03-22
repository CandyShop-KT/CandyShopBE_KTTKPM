package com.example.demo.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ApiResponseErrorDTO;
import com.example.demo.dto.ApiResponseNoDataDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.PublisherRequestDTO;
import com.example.demo.dto.PublisherRequestUpdateDTO;
import com.example.demo.model.Publisher;
import com.example.demo.service.PublisherService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

	private static final Logger logger = LoggerFactory.getLogger(PublisherController.class);
	private PublisherService publisherService;

	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getAllPublishers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "publisherName") String sortField,
			@RequestParam(defaultValue = "asc") String sortOrder) {
		PagedResponseDTO<Publisher> publishers = publisherService.getAllPublishers(page, limit, sortField, sortOrder);
		ApiResponseDTO<PagedResponseDTO<Publisher>> response = new ApiResponseDTO<>("Publishers retrieved successfully",
				HttpStatus.OK.value(), publishers);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{publisherId}")
	public ResponseEntity<?> getPublisher(@PathVariable String publisherId) {
		Publisher publisher = publisherService.getPublisher(publisherId);
		ApiResponseDTO<Publisher> response = new ApiResponseDTO<>("Publisher retrieved successfully",
				HttpStatus.OK.value(), publisher);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> createPublisher(@Valid @RequestBody PublisherRequestDTO publisherRequestDTO,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		Publisher publisher = publisherService.createPublisher(publisherRequestDTO);
		ApiResponseDTO<Publisher> response = new ApiResponseDTO<>("Publisher created successfully",
				HttpStatus.CREATED.value(), publisher);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable String publisherId,
			@Valid @RequestBody PublisherRequestUpdateDTO publisherRequestUpdateDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		Publisher publisher = publisherService.updatePublisher(publisherId, publisherRequestUpdateDTO);
		ApiResponseDTO<Publisher> response = new ApiResponseDTO<>("Publisher updated successfully",
				HttpStatus.OK.value(), publisher);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable String publisherId) {
		publisherService.deletePublisher(publisherId);
		ApiResponseNoDataDTO response = new ApiResponseNoDataDTO("Publisher deleted successfully",
				HttpStatus.OK.value());
		return ResponseEntity.ok(response);
	}

}
