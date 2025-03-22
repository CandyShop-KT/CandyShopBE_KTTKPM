package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "price_history")
public class PriceHistory {
	
	@Id
	@Column(name = "private_history_id")
	private String privateHistoryId;
	
	@Column(name = "new_price", nullable = false)
	private double newPrice;
	
	@Column(name = "price_change_reason", nullable = true)
	private String priceChangeReason;
	
	@Column(name = "price_change_effective_date", nullable = false, unique = true)
	private LocalDateTime priceChangeEffectiveDate;
	
	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;
	
	@Column(name = "updated_date", nullable = false)
	private LocalDateTime updatedDate;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "product_id", nullable = false)
	@JsonIgnore
	private Product product;
	
	@OneToMany(mappedBy = "priceHistory", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
	
	@PrePersist
	public void prePersist() {
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
		this.privateHistoryId = UUID.randomUUID().toString();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedDate = LocalDateTime.now();
	}
}
