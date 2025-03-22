package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
import com.example.demo.dto.ApiResponseNoDataDTO;
import com.example.demo.dto.DistrictRequestDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.model.District;
import com.example.demo.model.Ward;
import com.example.demo.service.DistrictService;
import com.example.demo.service.WardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/districts")
public class DistrictController {
	
	private static final Logger logger = LoggerFactory.getLogger(DistrictController.class);
	private DistrictService districtService;
	private WardService wardService;

	public DistrictController(DistrictService districtService, WardService wardService) {
		this.districtService = districtService;
		this.wardService = wardService;
	}

	@GetMapping("/{idDistrict}")
	public ResponseEntity<?> getDistrict(@PathVariable String idDistrict) throws Exception {
		District district = districtService.getDistrict(idDistrict);
		ApiResponseDTO<District> response = new ApiResponseDTO<>("Get district successfully", HttpStatus.OK.value(),
				district);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{districtId}/wards")
	public ResponseEntity<?> getWardsByDistrictId(@PathVariable String districtId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "wardName") String sortField,
			@RequestParam(defaultValue = "asc") String sortOder) throws Exception {
		PagedResponseDTO<Ward> wards = wardService.getWardsByDistrictId(districtId, page, limit, sortField, sortOder);
		ApiResponseDTO<PagedResponseDTO<Ward>> response = new ApiResponseDTO<>("Get wards successfully",
				HttpStatus.OK.value(), wards);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{idProvince}")
	public ResponseEntity<?> createDistrict(@PathVariable String idProvince,
			@Valid @RequestBody DistrictRequestDTO districtRequestDTO, BindingResult bindingResult)
			throws ResourceConflictException, Exception {
		if (bindingResult.hasErrors()) {
			Map<String, Object> errors = new LinkedHashMap<>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return ResponseEntity.badRequest().body(response);
		}
		District district = districtService.createDistrict(idProvince, districtRequestDTO);
		ApiResponseDTO<District> response = new ApiResponseDTO<>("Create district successfully",
				HttpStatus.CREATED.value(), district);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{idDistrict}")
	public ResponseEntity<?> updateDistrict(@PathVariable String idDistrict,
			@Valid @RequestBody DistrictRequestDTO districtRequestDTO, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			Map<String, Object> errors = new LinkedHashMap<>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, Object>> response = new ApiResponseDTO<>("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return ResponseEntity.badRequest().body(response);
		}
		District district = districtService.updateDistrict(idDistrict, districtRequestDTO);
		ApiResponseDTO<District> response = new ApiResponseDTO<>("Update district successfully", HttpStatus.OK.value(),
				district);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{idDistrict}")
	public ResponseEntity<?> deleteDistrict(@PathVariable String idDistrict) throws Exception {
		logger.info("Delete district with id: " + idDistrict);
		districtService.deleteDistrict(idDistrict);
		logger.info("Delete district successfully with id: " + idDistrict + " and all wards in this district");
		return ResponseEntity.ok(new ApiResponseNoDataDTO("Delete district successfully", HttpStatus.OK.value()));
	}
}
