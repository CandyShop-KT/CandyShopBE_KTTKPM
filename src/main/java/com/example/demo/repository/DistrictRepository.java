package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, String>{

	boolean existsByDistrictName(String districtName);
	
	Page<District> findByProvinceProvinceId(String provinceId, Pageable pageable);
	
}
