package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeEmailRequestDTO {
	
	@NotBlank(message = "New email is required")
	@Email(message = "New email is invalid")
	private String newEmail;
	
	@NotBlank(message = "Password is required")
	private String password;
	
	@NotBlank(message = "OTP is required")
	@Size(min = 6, max = 6, message = "OTP must be 6 characters")
	@Pattern(regexp = "^[0-9]{6,6}$", message = "OTP must be numeric")
	private String otp;
}
