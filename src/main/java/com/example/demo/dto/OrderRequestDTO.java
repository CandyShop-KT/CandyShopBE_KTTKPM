package com.example.demo.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
	
private String note;
	
	@NotBlank(message = "Address is required")
	@Size(min = 1, max = 255, message = "Address must be between 1 and 255 characters")
	private String address;
	
	@NotBlank(message = "Customer name is required")
	@Size(min = 2, max = 50, message = "Customer name must be between 2 and 50 characters")
	@Pattern(regexp = "^[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*(?:[ ][A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*)*$", message = "First name must be alphabetic and start with a capital letter")
	private String customerName;
	
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "(84|0[3|5|7|8|9])([0-9]{8})\\b", message = "Phone number is invalid")
	private String phoneNumber;
	
	@NotBlank(message = "Province is required")
	private String provinceId;
	
	@NotBlank(message = "District is required")
	private String districtId;
	
	@NotBlank(message = "Ward is required")
	private String wardId;
	
	@NotBlank(message = "User is required")
	private String userId;
	
	@NotNull(message = "Order details is required")
	@Valid
	private List<OrderDetailRequest> orderDetails;
}
