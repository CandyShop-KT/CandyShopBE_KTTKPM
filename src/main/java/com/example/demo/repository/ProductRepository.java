package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{
    Page<Product> findBySubCategorySubCategoryId(String subCategoryId, Pageable pageable);
    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.priceHistories ph WHERE ph.newPrice BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, Pageable pageable);
}