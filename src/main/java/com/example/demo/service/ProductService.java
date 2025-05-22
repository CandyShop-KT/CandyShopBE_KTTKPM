package com.example.demo.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.PriceHistoryRequestDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductRequestUpdateDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.model.PriceHistory;
import com.example.demo.model.Product;

public interface ProductService {

	public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) throws IOException, Exception;

	public ProductResponseDTO getProductResponse(String id);

	public Product findProduct(String id);

	public ProductResponseDTO updateProduct(String id, ProductRequestUpdateDTO productRequestUpdateDTO);

	public void deleteProduct(String id) throws Exception;

	public PagedResponseDTO<ProductResponseDTO> getProducts(int page, int limit, String sortField, String sortOrder);

	public ProductResponseDTO updateProductMainImage(String id, MultipartFile mainImage) throws IOException, Exception;

	public ProductResponseDTO updateProductImages(String id, MultipartFile[] images) throws IOException, Exception;

	public PriceHistory createPriceHistory(String productId, PriceHistoryRequestDTO priceHistoryRequestDTO);

	public PagedResponseDTO<PriceHistory> getPriceHistoriesByProductId(String productId, int page, int size,
			String sortField, String sortOder);

	public PriceHistory getCurrentPriceProductByProductId(String productId);

	public PagedResponseDTO<ProductResponseDTO> getProductsBySubCategory(String subCategoryId, int page, int limit,
			String sortField, String sortOrder);

	public PagedResponseDTO<ProductResponseDTO> getProductsByName(String name, int page, int limit, String sortField,
			String sortOrder);

	public PagedResponseDTO<ProductResponseDTO> getProductsByPriceRange(double minPrice, double maxPrice, int page,
			int limit, String sortField, String sortOrder);
	public PagedResponseDTO<ProductResponseDTO> getProductsBySubCategory(String subCategoryId, int page, int limit, String sortBy);

}