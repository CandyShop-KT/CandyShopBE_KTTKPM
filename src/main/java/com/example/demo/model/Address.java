package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "address")
public class Address {
	
	@Id
	@Column(name = "address_id")
	private String addressId;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "customer_name", nullable = false)
	private String customerName;
	
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "province_id")
	private Province province;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "district_id")
	private District district;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ward_id")
	private Ward ward;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void prePersist() {
		this.addressId = UUID.randomUUID().toString();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
