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
public class PublisherRequestDTO {
	
	@NotBlank(message = "Publisher name is required")
	@Size(min = 2, max = 50, message = "Publisher name must be between 2 and 50 characters")
	private String publisherName;
	
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "(84|0[3|5|7|8|9])([0-9]{8})\\b", message = "Phone number is invalid")
	private String phoneNumber;
	
	@NotBlank(message = "Address is required")
	@Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
	private String address;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email is invalid")
	private String email;
}
