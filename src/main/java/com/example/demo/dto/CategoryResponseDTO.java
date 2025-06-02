package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.SubCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private String categoryId;
    private String categoryName;
    private List<SubCategory> subCategories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}