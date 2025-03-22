package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.Image;
import com.example.demo.model.PriceHistory;
import com.example.demo.model.Publisher;
import com.example.demo.model.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
	private String productId;
	private String productName;
	private String description;
	private String dimension;
	private double weight;
	private String mainImageUrl;
	private SubCategory subCategory;
	private Publisher publisher;
	private PriceHistory currentPrice;
	private List<Image> images = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
