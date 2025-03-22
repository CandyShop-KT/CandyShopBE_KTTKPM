package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WardRequestDTO {
	
	@NotBlank(message = "Ward name is required")
	@Size(min = 1, max = 100, message = "Ward name must be between 1 and 100 characters")
	private String wardName;
	
}
