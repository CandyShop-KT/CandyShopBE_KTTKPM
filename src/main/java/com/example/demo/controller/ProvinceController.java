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
import com.example.demo.dto.ProvinceRequestDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.District;
import com.example.demo.model.Province;
import com.example.demo.service.DistrictService;
import com.example.demo.service.ProvinceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

	private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);
	private ProvinceService provinceService;
	private DistrictService districtService;

	public ProvinceController(ProvinceService provinceService, DistrictService districtService) {
		this.provinceService = provinceService;
		this.districtService = districtService;
	}

	@GetMapping
	public ResponseEntity<?> getProvinces(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "provinceName") String sortField,
			@RequestParam(defaultValue = "asc") String sortOder) throws Exception {
		PagedResponseDTO<Province> pagedResponseDTO = provinceService.getProvinces(page, limit, sortField, sortOder);
		return ResponseEntity.ok(new ApiResponseDTO<PagedResponseDTO<Province>>("Get provinces success!",
				HttpStatus.OK.value(), pagedResponseDTO));
	}

	@GetMapping("/{idProvince}")
	public ResponseEntity<?> getProvince(@PathVariable String idProvince) throws ResourceNotFoundException, Exception {
		Province province = provinceService.getProvince(idProvince);
		return ResponseEntity
				.ok(new ApiResponseDTO<Province>("Get province success!", HttpStatus.OK.value(), province));
	}

	@GetMapping("/{idProvince}/districts")
	public ResponseEntity<?> getDistricts(@PathVariable String idProvince, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "districtName") String sortField,
			@RequestParam(defaultValue = "asc") String sortOder) throws Exception {
		PagedResponseDTO<District> districts = districtService.getDistrictsByProvinceId(idProvince, page, limit,
				sortField, sortOder);
		ApiResponseDTO<PagedResponseDTO<District>> response = new ApiResponseDTO<>("Get districts successfully",
				HttpStatus.OK.value(), districts);
		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> createProvince(@Valid @RequestBody ProvinceRequestDTO provinceRequestDTO,
			BindingResult bindingResult) throws ResourceConflictException, Exception {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		Province myProvince = provinceService.createProvince(provinceRequestDTO);
		ApiResponseDTO<Province> apiResponseDTO = new ApiResponseDTO<Province>("Create province success!",
				HttpStatus.OK.value(), myProvince);
		return new ResponseEntity<ApiResponseDTO<Province>>(apiResponseDTO, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{idProvince}")
	public ResponseEntity<?> updateProvince(@PathVariable String idProvince,
			@Valid @RequestBody ProvinceRequestDTO provinceRequestDTO, BindingResult bindingResult)
			throws ResourceNotFoundException, ResourceConflictException, Exception {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			ApiResponseErrorDTO apiResponseErrorDTO = new ApiResponseErrorDTO("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return new ResponseEntity<ApiResponseErrorDTO>(apiResponseErrorDTO, HttpStatus.BAD_REQUEST);
		}
		Province myProvince = provinceService.updateProvince(idProvince, provinceRequestDTO);
		return ResponseEntity
				.ok(new ApiResponseDTO<Province>("Update province success!", HttpStatus.OK.value(), myProvince));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{idProvince}")
	public ResponseEntity<?> deleteProvince(@PathVariable String idProvince)
			throws ResourceNotFoundException, Exception {
		provinceService.deleteProvince(idProvince);
		return ResponseEntity.ok(new ApiResponseNoDataDTO("Delete province success!", HttpStatus.OK.value()));
	}

}
