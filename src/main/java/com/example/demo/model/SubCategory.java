package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_category")
public class SubCategory {
	
	@Id
	@Column(name = "sub_category_id")
	private String subCategoryId;
	
	@Column(name = "sub_category_name", nullable = false, unique = true)
	private String subCategoryName;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private Category category;
	
	@OneToMany(mappedBy = "subCategory" ,cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<Product> products = new ArrayList<Product>();
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.subCategoryId = UUID.randomUUID().toString();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
