package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryRequestDTO {

	@NotBlank(message = "Sub category name is required")
	private String subCategoryName;

	@NotBlank(message = "Category id is required")
	private String categoryId;
}
