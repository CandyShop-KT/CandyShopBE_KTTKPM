package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryRequestDTO {
	
	@NotBlank(message = "SubCategory name is required")
	@Size(min = 2, max = 50, message = "SubCategory name must be between 2 and 50 characters")
	private String subCategoryName;
}
