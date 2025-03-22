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
import com.example.demo.dto.WardRequestDTO;
import com.example.demo.model.Ward;
import com.example.demo.service.WardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wards")
public class WardController {
	
	private static final Logger logger = LoggerFactory.getLogger(WardController.class);
	private WardService wardService;

	public WardController(WardService wardService) {
		this.wardService = wardService;
	}

	@GetMapping("/{wardId}")
	public ResponseEntity<?> getWard(@PathVariable String wardId) throws Exception {
		Ward ward = wardService.getWard(wardId);
		ApiResponseDTO<Ward> response = new ApiResponseDTO<>("Get ward successfully", HttpStatus.OK.value(), ward);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{districtId}")
	public ResponseEntity<?> createWard(@PathVariable String districtId,
			@Valid @RequestBody WardRequestDTO wardRequestDTO, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		Ward ward = wardService.createWard(districtId, wardRequestDTO);
		ApiResponseDTO<Ward> response = new ApiResponseDTO<>("Create ward successfully", HttpStatus.CREATED.value(),
				ward);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{wardId}")
	public ResponseEntity<?> updateWard(@PathVariable String wardId, @Valid @RequestBody WardRequestDTO wardRequestDTO,
			BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		Ward ward = wardService.updateWard(wardId, wardRequestDTO);
		ApiResponseDTO<Ward> response = new ApiResponseDTO<>("Update ward successfully", HttpStatus.OK.value(), ward);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{wardId}")
	public ResponseEntity<?> deleteWard(@PathVariable String wardId) throws Exception {
		wardService.deleteWard(wardId);
		ApiResponseNoDataDTO response = new ApiResponseNoDataDTO("Delete ward successfully", HttpStatus.OK.value());
		return ResponseEntity.ok(response);
	}
}
