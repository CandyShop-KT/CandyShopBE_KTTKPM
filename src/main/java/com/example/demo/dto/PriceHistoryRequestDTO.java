package com.example.demo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistoryRequestDTO {
	
	@DecimalMin(value = "1.00", message = "Price must be at least 1.00")
	private double newPrice;
	
	private String priceChangeReason;
	
	@FutureOrPresent(message = "Price change effective date must be in the present or future")
	private LocalDateTime priceChangeEffectiveDate;
}
