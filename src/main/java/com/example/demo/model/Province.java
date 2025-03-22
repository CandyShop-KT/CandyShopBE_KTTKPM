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
@Table(name = "province")
public class Province {
	
	@Id
	@Column(name = "province_id")
	private String provinceId;
	
	@Column(name = "province_name", nullable = false, unique = true)
	private String provinceName;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<Address> addresses = new ArrayList<Address>();
	
	@OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<Order> orders = new ArrayList<Order>();
	
	@OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<District> districts = new ArrayList<District>();
	
	@PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.provinceId = UUID.randomUUID().toString();
    }
	
	@PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
	
}
