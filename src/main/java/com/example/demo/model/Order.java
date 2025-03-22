package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_order")
public class Order {
	
	@Id
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "total_amount", nullable = false)
	private double totalAmount;
	
	@Column(name = "note", nullable = true)
	private String note;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "customer_name", nullable = false)
	private String customerName;
	
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "province_id", nullable = false)
	private Province province;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "district_id", nullable = false)
	private District district;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ward_id", nullable = false)
	private Ward ward;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OrderStatus status;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 @JsonManagedReference
	private List<OrderDetail> orderDetails = new ArrayList<>();
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.orderId = UUID.randomUUID().toString();
		this.status = OrderStatus.PENDING_CONFIRMATION;
		this.totalAmount = calculateTotalAmount();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	private double calculateTotalAmount() {
		return this.orderDetails.stream().mapToDouble(OrderDetail::getSubTotal).sum();
	}
}
