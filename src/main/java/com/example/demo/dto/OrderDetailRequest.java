package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {
	
	@NotBlank(message = "Product id is required")
	private String productId;
	
	@Positive(message = "Quantity must be positive")
	@Min(value = 1, message = "Quantity must be greater than 0")
	private int quantity;
	
	@NotBlank(message = "Price history id is required")
	private String priceHistoryId;
	
}
