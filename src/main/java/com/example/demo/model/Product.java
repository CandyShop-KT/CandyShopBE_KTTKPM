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
@Table(name = "product")
public class Product {
	
	@Id
	@Column(name = "product_id")
	private String productId;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Column(name = "description", nullable = true)
	private String description;
	
	@Column(name = "dimension", nullable = false)
	private String dimension;
	
	@Column(name = "weight", nullable = false)
	private double weight;
	
	@Column(name = "main_image_name", nullable = false)
	private String mainImageName;
	
	@Column(name = "main_image_url", nullable = false)
	private String mainImageUrl;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "sub_category_id", nullable = false)
	private SubCategory subCategory;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "publisher_id", nullable = false)
	private Publisher publisher;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Image> images = new ArrayList<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<PriceHistory> priceHistories = new ArrayList<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<OrderDetail> orderDetails = new ArrayList<>();
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.productId = UUID.randomUUID().toString();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
