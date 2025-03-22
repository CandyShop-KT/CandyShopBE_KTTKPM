package com.example.demo.service.imp;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.PriceHistoryRequestDTO;
import com.example.demo.dto.ProductRequestDTO;
import com.example.demo.dto.ProductRequestUpdateDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Image;
import com.example.demo.model.PriceHistory;
import com.example.demo.model.Product;
import com.example.demo.model.Publisher;
import com.example.demo.model.SubCategory;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.PublisherService;
import com.example.demo.service.S3Service;
import com.example.demo.service.SubCategoryService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImp implements ProductService {

	private ProductRepository productRepository;
	private SubCategoryService subCategoryService;
	private PublisherService publisherService;
	private S3Service s3Service;
	private PriceHistoryRepository priceHistoryRepository;
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public ProductServiceImp(ProductRepository productRepository, SubCategoryService subCategoryService,
			PublisherService publisherService, S3Service s3Service, PriceHistoryRepository priceHistoryRepository) {
		this.productRepository = productRepository;
		this.subCategoryService = subCategoryService;
		this.publisherService = publisherService;
		this.s3Service = s3Service;
		this.priceHistoryRepository = priceHistoryRepository;
	}

	@Override
	@Transactional
	public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) throws IOException, Exception {
		String avatarName = null;
		try {
			Product product = new Product();
			product.setProductName(productRequestDTO.getProductName());
			product.setDescription(productRequestDTO.getDescription());
			product.setDimension(productRequestDTO.getDimension());
			product.setWeight(productRequestDTO.getWeight());

			String subCategoryId = productRequestDTO.getSubCategoryId();
			SubCategory subCategory = subCategoryService.getSubCategory(subCategoryId);

			String publisherId = productRequestDTO.getPublisherId();
			Publisher publisher = publisherService.getPublisher(publisherId);

			product.setSubCategory(subCategory);
			product.setPublisher(publisher);

			double price = productRequestDTO.getPrice();
			PriceHistory priceHistory = new PriceHistory();
			priceHistory.setNewPrice(price);
			priceHistory.setPriceChangeReason("Initial price set up for new product");
			priceHistory.setPriceChangeEffectiveDate(LocalDateTime.now());
			priceHistory.setProduct(product);

			product.getPriceHistories().add(priceHistory);

			MultipartFile mainImage = productRequestDTO.getMainImage();
			avatarName = s3Service.uploadFile(mainImage);
			String avatarUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "ap-southeast-1",
					avatarName);
			product.setMainImageName(avatarName);
			product.setMainImageUrl(avatarUrl);
			Product myProduct = productRepository.save(product);
			return convertProductToProductResponseDTO(myProduct);
		} catch (Exception e) {
			if (avatarName != null)
				s3Service.deleteFile(avatarName);
			throw e;
		}
	}

	@Override
	public ProductResponseDTO getProductResponse(String id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		return convertProductToProductResponseDTO(product);
	}

	@Override
	@Transactional
	public ProductResponseDTO updateProduct(String id, ProductRequestUpdateDTO productRequestUpdateDTO) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		if (productRequestUpdateDTO.getProductName() != null)
			product.setProductName(productRequestUpdateDTO.getProductName());
		if (productRequestUpdateDTO.getDescription() != null)
			product.setDescription(productRequestUpdateDTO.getDescription());
		if (productRequestUpdateDTO.getDimension() != null)
			product.setDimension(productRequestUpdateDTO.getDimension());
		if (productRequestUpdateDTO.getWeight() != null)
			product.setWeight(productRequestUpdateDTO.getWeight().doubleValue());
		if (productRequestUpdateDTO.getSubCategoryId() != null) {
			SubCategory subCategory = subCategoryService.getSubCategory(productRequestUpdateDTO.getSubCategoryId());
			product.setSubCategory(subCategory);
		}
		if (productRequestUpdateDTO.getPublisherId() != null) {
			Publisher publisher = publisherService.getPublisher(productRequestUpdateDTO.getPublisherId());
			product.setPublisher(publisher);
		}
		Product myProduct = productRepository.save(product);
		return convertProductToProductResponseDTO(myProduct);
	}

	@Override
	@Transactional
	public void deleteProduct(String id) throws Exception {
		try {
			Product product = productRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
			s3Service.deleteFile(product.getMainImageName());
			for (Image image : product.getImages()) {
				s3Service.deleteFile(image.getImageTitle());
			}
			productRepository.delete(product);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
    public PagedResponseDTO<ProductResponseDTO> getProductsBySubCategory(String subCategoryId, int page, int limit, String sortField, String sortOrder) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        
        // Lấy danh sách sản phẩm theo subcategory
        Page<Product> products = productRepository.findBySubCategorySubCategoryId(subCategoryId, pageable);
        
        // Chuyển đổi sang DTO
        List<ProductResponseDTO> productResponseDTOs = products.getContent().stream()
		.map(this::convertProductToProductResponseDTO).toList();
        
        return new PagedResponseDTO<>(productResponseDTOs, products.getNumber(), products.getSize(), products.getTotalElements(), products.getTotalPages());
    }

	@Override
	public PagedResponseDTO<ProductResponseDTO> getProducts(int page, int limit, String sortField, String sortOrder) {
		Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending()
				: Sort.by(sortField).ascending();
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<Product> pageProduct = productRepository.findAll(pageable);
		PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
		List<ProductResponseDTO> content = pageProduct.getContent().stream()
				.map(this::convertProductToProductResponseDTO).toList();
		pagedResponseDTO.setContent(content);
		pagedResponseDTO.setTotalElements(pageProduct.getTotalElements());
		pagedResponseDTO.setTotalPages(pageProduct.getTotalPages());
		pagedResponseDTO.setPageNumber(pageProduct.getNumber());
		pagedResponseDTO.setPageSize(pageProduct.getSize());
		return pagedResponseDTO;
	}

	@Override
	@Transactional
	public ProductResponseDTO updateProductMainImage(String id, MultipartFile mainImage) throws Exception {
		if(mainImage == null) {
			throw new BadRequestException("mainImage", "Main image is required");
		}
		String avatarName = null;
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		try {
			avatarName = s3Service.uploadFile(mainImage);
			String avatarUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "ap-southeast-1",
					avatarName);
			s3Service.deleteFile(product.getMainImageName());
			product.setMainImageName(avatarName);
			product.setMainImageUrl(avatarUrl);
		} catch (Exception e) {
			if (avatarName != null)
				s3Service.deleteFile(avatarName);
			throw e;
		}
		Product myProduct = productRepository.save(product);
		return convertProductToProductResponseDTO(myProduct);
	}

	@Override
	@Transactional
	public ProductResponseDTO updateProductImages(String id, MultipartFile[] images) throws IOException, Exception {
		if (images == null || images.length == 0) {
			throw new BadRequestException("images", "Images are required");
		}
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		if (product.getImages().size() + images.length > 5)
			throw new BadRequestException("images", "Maximum 5 images are allowed");
		List<Image> imageList = new ArrayList<Image>();
		try {
			for (MultipartFile image : images) {
				String imageName = s3Service.uploadFile(image);
				String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "ap-southeast-1",
						imageName);
				Image imageObj = new Image();
				imageObj.setImageTitle(imageName);
				imageObj.setUrl(imageUrl);
				imageObj.setProduct(product);
				imageList.add(imageObj);
			}
			product.getImages().addAll(imageList);
			product = productRepository.save(product);
		} catch (Exception e) {
			for (Image image : imageList) {
				s3Service.deleteFile(image.getImageTitle());
			}
			throw e;
		}
		return convertProductToProductResponseDTO(product);
	}

	@Override
	@Transactional
	public PriceHistory createPriceHistory(String productId, PriceHistoryRequestDTO priceHistoryRequestDTO) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		PriceHistory priceHistory = new PriceHistory();
		priceHistory.setNewPrice(priceHistoryRequestDTO.getNewPrice());
		priceHistory.setPriceChangeReason(priceHistoryRequestDTO.getPriceChangeReason());
		priceHistory.setPriceChangeEffectiveDate(priceHistoryRequestDTO.getPriceChangeEffectiveDate());
		priceHistory.setProduct(product);
		return priceHistoryRepository.save(priceHistory);
	}

	@Override
	public PagedResponseDTO<PriceHistory> getPriceHistoriesByProductId(String productId, int page, int size,
			String sortField, String sortOder) {
		Sort sort = sortOder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<PriceHistory> pagePriceHistory = priceHistoryRepository.findAll(pageable);

		PagedResponseDTO<PriceHistory> pagedResponseDTO = new PagedResponseDTO<PriceHistory>();
		pagedResponseDTO.setContent(pagePriceHistory.getContent());
		pagedResponseDTO.setTotalPages(pagePriceHistory.getTotalPages());
		pagedResponseDTO.setTotalElements(pagePriceHistory.getTotalElements());
		pagedResponseDTO.setPageSize(pagePriceHistory.getSize());
		pagedResponseDTO.setPageNumber(pagePriceHistory.getNumber());

		return pagedResponseDTO;
	}

	@Override
public PriceHistory getCurrentPriceProductByProductId(String productId) {
    // Lấy danh sách PriceHistory
    List<PriceHistory> priceHistories = priceHistoryRepository
            .findCurrentPriceProductByProduct(productId, LocalDateTime.now());

    // Nếu danh sách trống, ném ngoại lệ
    if (priceHistories.isEmpty()) {
        throw new ResourceNotFoundException("Price history not found");
    }

    // Lấy bản ghi đầu tiên
    return priceHistories.get(0);
}


private ProductResponseDTO convertProductToProductResponseDTO(Product product) {
    // Lấy danh sách PriceHistory
    List<PriceHistory> priceHistories = priceHistoryRepository
            .findCurrentPriceProductByProduct(product.getProductId(), LocalDateTime.now());

    // Kiểm tra nếu không có kết quả nào
    if (priceHistories.isEmpty()) {
        throw new ResourceNotFoundException("Price history not found");
    }

    // Lấy bản ghi đầu tiên
    PriceHistory priceHistory = priceHistories.get(0);

    // Tạo ProductResponseDTO và gán các giá trị
    ProductResponseDTO productResponseDTO = new ProductResponseDTO();
    productResponseDTO.setProductId(product.getProductId());
    productResponseDTO.setProductName(product.getProductName());
    productResponseDTO.setDescription(product.getDescription());
    productResponseDTO.setDimension(product.getDimension());
    productResponseDTO.setWeight(product.getWeight());
    productResponseDTO.setMainImageUrl(product.getMainImageUrl());
    productResponseDTO.setSubCategory(product.getSubCategory());
    productResponseDTO.setPublisher(product.getPublisher());
    productResponseDTO.setCurrentPrice(priceHistory); // Gán PriceHistory vào ProductResponseDTO
    productResponseDTO.setImages(product.getImages());
    productResponseDTO.setCreatedAt(product.getCreatedAt());
    productResponseDTO.setUpdatedAt(product.getUpdatedAt());

    return productResponseDTO;
}


	@Override
	public Product findProduct(String id) {
		return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	@Override
public PagedResponseDTO<ProductResponseDTO> getProductsByName(String name, int page, int limit, String sortField, String sortOrder) {
    Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
    Pageable pageable = PageRequest.of(page, limit, sort);
    Page<Product> pageProduct = productRepository.findByProductNameContainingIgnoreCase(name, pageable);

    List<ProductResponseDTO> content = pageProduct.getContent().stream()
            .map(this::convertProductToProductResponseDTO).toList();

    PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
    pagedResponseDTO.setContent(content);
    pagedResponseDTO.setTotalElements(pageProduct.getTotalElements());
    pagedResponseDTO.setTotalPages(pageProduct.getTotalPages());
    pagedResponseDTO.setPageNumber(pageProduct.getNumber());
    pagedResponseDTO.setPageSize(pageProduct.getSize());

    return pagedResponseDTO;
}

@Override
public PagedResponseDTO<ProductResponseDTO> getProductsByPriceRange(double minPrice, double maxPrice, int page, int limit, String sortField, String sortOrder) {
    Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
    Pageable pageable = PageRequest.of(page, limit, sort);
    Page<Product> pageProduct = productRepository.findByPriceRange(minPrice, maxPrice, pageable);

    List<ProductResponseDTO> content = pageProduct.getContent().stream()
            .map(this::convertProductToProductResponseDTO).toList();

    PagedResponseDTO<ProductResponseDTO> pagedResponseDTO = new PagedResponseDTO<>();
    pagedResponseDTO.setContent(content);
    pagedResponseDTO.setTotalElements(pageProduct.getTotalElements());
    pagedResponseDTO.setTotalPages(pageProduct.getTotalPages());
    pagedResponseDTO.setPageNumber(pageProduct.getNumber());
    pagedResponseDTO.setPageSize(pageProduct.getSize());

    return pagedResponseDTO;
}


}