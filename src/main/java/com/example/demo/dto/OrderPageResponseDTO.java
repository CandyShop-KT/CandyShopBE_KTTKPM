package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.model.District;
import com.example.demo.model.Province;
import com.example.demo.model.Ward;
import com.example.demo.model.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageResponseDTO {
	private String orderId;
	private String address;
	private Province province;
	private District district;
	private Ward wardId;
	private String note;
	private String customerName;
	private String phoneNumber;
	private double totalAmount;
	private OrderStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
