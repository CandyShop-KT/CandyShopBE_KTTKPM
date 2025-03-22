package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictRequestDTO {
	
	@NotBlank(message = "District name is required")
	@Size(min = 1, max = 50, message = "District name must be between 1 and 50 characters")
	private String districtName;
}
