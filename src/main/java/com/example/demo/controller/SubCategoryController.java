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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ApiResponseErrorDTO;
import com.example.demo.dto.ApiResponseNoDataDTO;
import com.example.demo.dto.SubCategoryRequestDTO;
import com.example.demo.model.SubCategory;
import com.example.demo.service.SubCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

	private static final Logger logger = LoggerFactory.getLogger(SubCategoryController.class);
	private SubCategoryService subCategoryService;

	public SubCategoryController(SubCategoryService subCategoryService) {
		this.subCategoryService = subCategoryService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{categoryId}")
	public ResponseEntity<?> createSubCategory(@PathVariable String categoryId,
			@Valid @RequestBody SubCategoryRequestDTO categoryRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		SubCategory subCategory = subCategoryService.createSubCategory(categoryId, categoryRequestDTO);
		ApiResponseDTO<SubCategory> response = new ApiResponseDTO<>("SubCategory created successfully",
				HttpStatus.CREATED.value(), subCategory);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{subCategoryId}")
	public ResponseEntity<?> updateSubCategory(@PathVariable String subCategoryId,
			@Valid @RequestBody SubCategoryRequestDTO subCategoryRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		SubCategory subCategory = subCategoryService.updateSubCategory(subCategoryId, subCategoryRequestDTO);
		ApiResponseDTO<SubCategory> response = new ApiResponseDTO<>("SubCategory updated successfully",
				HttpStatus.OK.value(), subCategory);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{subCategoryId}")
	public ResponseEntity<?> deleteSubCategory(@PathVariable String subCategoryId) {
		subCategoryService.deleteSubCategory(subCategoryId);
		ApiResponseNoDataDTO response = new ApiResponseNoDataDTO("SubCategory deleted successfully",
				HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/{subCategoryId}")
	public ResponseEntity<?> getSubCategory(@PathVariable String subCategoryId) {
		SubCategory subCategory = subCategoryService.getSubCategory(subCategoryId);
		ApiResponseDTO<SubCategory> response = new ApiResponseDTO<>("SubCategory retrieved successfully",
				HttpStatus.OK.value(), subCategory);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
