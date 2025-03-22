package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, String>{
	
	boolean existsByWardName(String wardName);
	
	Page<Ward> findByDistrictDistrictId(String districtId, Pageable pageable);
	
}
