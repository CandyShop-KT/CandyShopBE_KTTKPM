package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "order_detail")
public class OrderDetail {
	
	@Id
	@Column(name = "order_detail_id")
	private String orderDetailId;
	
	@Column(name = "quantity", nullable = false)
	private double quantity;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "order_id", nullable = false)
	 @JsonBackReference
	private Order order;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "price_history_id", nullable = false)
	private PriceHistory priceHistory;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		orderDetailId = UUID.randomUUID().toString();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	public double getSubTotal() {
		return quantity * priceHistory.getNewPrice();
	}
}
