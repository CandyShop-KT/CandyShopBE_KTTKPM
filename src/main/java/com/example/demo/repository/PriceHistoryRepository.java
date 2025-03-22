package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.PriceHistory;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, String> {
	
	// @Query("SELECT p FROM PriceHistory p WHERE p.product.productId = :productId AND p.priceChangeEffectiveDate <= :currentDate ORDER BY p.priceChangeEffectiveDate DESC")
	// Optional<PriceHistory> findCurrentPriceProductByProduct(@Param("productId") String productId, @Param("currentDate") LocalDateTime currentDate);
	@Query("SELECT p FROM PriceHistory p WHERE p.product.productId = :productId AND p.priceChangeEffectiveDate <= :currentDate ORDER BY p.priceChangeEffectiveDate DESC")
	List<PriceHistory> findCurrentPriceProductByProduct(@Param("productId") String productId, @Param("currentDate") LocalDateTime currentDate);
}