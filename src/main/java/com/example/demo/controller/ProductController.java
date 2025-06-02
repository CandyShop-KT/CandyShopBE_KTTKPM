package com.example.demo.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ApiResponseNoDataDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.PriceHistoryRequestDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductRequestUpdateDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.model.PriceHistory;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable String productId) {
		ProductResponseDTO product = productService.getProductResponse(productId);
		ApiResponseDTO<ProductResponseDTO> apiResponseDTO = new ApiResponseDTO<>("Get product success",
				HttpStatus.OK.value(), product);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);

	}

//	@GetMapping("/subcategory/{subCategoryId}")
//	public ResponseEntity<?> getProductsBySubCategory(
//			@PathVariable String subCategoryId,
//			@RequestParam(defaultValue = "0") int page,
//			@RequestParam(defaultValue = "10") int limit,
//			@RequestParam(defaultValue = "productName") String sortField,
//			@RequestParam(defaultValue = "asc") String sortOrder) {
//
//		PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = productService.getProductsBySubCategory(subCategoryId,
//				page, limit, sortField, sortOrder);
//		ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>> apiResponseDTO = new ApiResponseDTO<>(
//				"Get all product success", HttpStatus.OK.value(), pagedResponseDTO);
//		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
//	}
	@GetMapping("/subcategory/{subCategoryId}")
	public ResponseEntity<?> getProductsBySubCategory(
	        @PathVariable String subCategoryId,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int limit,
	        @RequestParam(defaultValue = "productName_asc") String sortBy
	) {
	    var result = productService.getProductsBySubCategory(subCategoryId, page, limit, sortBy);
	    return ResponseEntity.ok(new ApiResponseDTO<>("Success", 200, result));
	}


	@GetMapping
	public ResponseEntity<?> getAllProduct(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "productName") String sortField,
			@RequestParam(defaultValue = "asc") String sortOder) {
		PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = productService.getProducts(page, limit, sortField,
				sortOder);
		ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>> apiResponseDTO = new ApiResponseDTO<>(
				"Get all product success", HttpStatus.OK.value(), pagedResponseDTO);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductRequestDTO productRequestDTO,
			BindingResult bindingResult) throws IOException, Exception {
		if (bindingResult.hasErrors()) {
			Map<String, Object> errors = new LinkedHashMap<String, Object>();
			if (productRequestDTO.getMainImage().isEmpty())
				errors.put("mainImage", "Main image is required");
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, Object>> apiResponseDTO = new ApiResponseDTO<>("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDTO);
		}
		ProductResponseDTO product = productService.createProduct(productRequestDTO);
		ApiResponseDTO<ProductResponseDTO> apiResponseDTO = new ApiResponseDTO<>("Create product success",
				HttpStatus.CREATED.value(), product);
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable String productId) throws Exception {
		productService.deleteProduct(productId);
		ApiResponseNoDataDTO apiResponseDTO = new ApiResponseNoDataDTO("Delete product success", HttpStatus.OK.value());
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable String productId,
			@Valid @ModelAttribute ProductRequestUpdateDTO productRequestUpdateDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, Object> errors = new LinkedHashMap<String, Object>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, Object>> apiResponseDTO = new ApiResponseDTO<>("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDTO);
		}
		ProductResponseDTO product = productService.updateProduct(productId, productRequestUpdateDTO);
		ApiResponseDTO<ProductResponseDTO> apiResponseDTO = new ApiResponseDTO<>("Update product success",
				HttpStatus.OK.value(), product);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}


	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{productId}/main-image")
	public ResponseEntity<?> updateProductMainImage(@PathVariable String productId,
			@RequestPart(value = "file") MultipartFile mainImage) throws IOException, Exception {
		ProductResponseDTO product = productService.updateProductMainImage(productId, mainImage);
		ApiResponseDTO<ProductResponseDTO> apiResponseDTO = new ApiResponseDTO<>("Update product main image success",
				HttpStatus.OK.value(), product);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{productId}/images")
	public ResponseEntity<?> updateProductImages(@PathVariable String productId,
			@RequestPart(value = "files") MultipartFile[] images) throws IOException, Exception {
		ProductResponseDTO product = productService.updateProductImages(productId, images);
		ApiResponseDTO<ProductResponseDTO> apiResponseDTO = new ApiResponseDTO<>("Update product images success",
				HttpStatus.OK.value(), product);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{productId}/price-histories")
	public ResponseEntity<?> createPriceHistory(@PathVariable String productId,
			@Valid @RequestBody PriceHistoryRequestDTO priceHistoryRequestDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, Object> errors = new LinkedHashMap<String, Object>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			ApiResponseDTO<Map<String, Object>> apiResponseDTO = new ApiResponseDTO<>("Validation failed",
					HttpStatus.BAD_REQUEST.value(), errors);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseDTO);
		}
		PriceHistory priceHistory = productService.createPriceHistory(productId, priceHistoryRequestDTO);
		ApiResponseDTO<PriceHistory> apiResponseDTO = new ApiResponseDTO<>("Add price history success",
				HttpStatus.CREATED.value(), priceHistory);
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponseDTO);
	}


	@GetMapping("/{productId}/price-histories")
	public ResponseEntity<?> getPriceHistoriesByProductId(@PathVariable String productId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int limit,
			@RequestParam(defaultValue = "priceChangeEffectiveDate") String sortField,
			@RequestParam(defaultValue = "desc") String sortOder) {
		PagedResponseDTO<PriceHistory> pagedResponseDTO = productService.getPriceHistoriesByProductId(productId, page,
				limit, sortField, sortOder);
		ApiResponseDTO<PagedResponseDTO<PriceHistory>> apiResponseDTO = new ApiResponseDTO<>(
				"Get price histories by product id success", HttpStatus.OK.value(), pagedResponseDTO);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}

	@GetMapping("/searchByName")
	public ResponseEntity<?> getProductsByName(@RequestParam String name,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "12") int limit,
			@RequestParam(defaultValue = "productName") String sortField,
			@RequestParam(defaultValue = "asc") String sortOrder) {
		PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = productService.getProductsByName(name, page, limit,
				sortField, sortOrder);
		ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>> apiResponseDTO = new ApiResponseDTO<>(
				"Search products by name success", HttpStatus.OK.value(), pagedResponseDTO);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}


	@GetMapping("/searchByPrice")
	public ResponseEntity<?> getProductsByPriceRange(@RequestParam double minPrice,
			@RequestParam double maxPrice,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "productName") String sortField,
			@RequestParam(defaultValue = "asc") String sortOrder) {
		PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = productService.getProductsByPriceRange(minPrice,
				maxPrice, page, limit, sortField, sortOrder);
		ApiResponseDTO<PagedResponseDTO<ProductResponseDTO>> apiResponseDTO = new ApiResponseDTO<>(
				"Search products by price range success", HttpStatus.OK.value(), pagedResponseDTO);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponseDTO);
	}
}