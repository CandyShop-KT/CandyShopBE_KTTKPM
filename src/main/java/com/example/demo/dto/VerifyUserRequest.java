package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyUserRequest {
	
	@NotBlank(message = "OTP is required")
	@Size(min = 6, max = 6, message = "OTP must be 6 characters long")
	@Pattern(regexp = "^[0-9]{6,6}$", message = "OTP must be a number")
	private String otp;
	
}
