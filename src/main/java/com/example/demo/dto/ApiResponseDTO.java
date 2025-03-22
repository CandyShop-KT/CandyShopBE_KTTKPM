package com.example.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiResponseDTO <T> extends ApiResponseNoDataDTO {
	
	public ApiResponseDTO(String message, int status, T data) {
		super(message, status);
		this.data = data;
	}
	
	private T data;
}
