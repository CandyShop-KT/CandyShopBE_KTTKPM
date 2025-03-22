package com.example.demo.service;

import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.ProvinceRequestDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Province;

public interface ProvinceService {
	
	Province createProvince(ProvinceRequestDTO provinceRequestDTO) throws Exception, ResourceConflictException;
	
	Province updateProvince(String provinceId, ProvinceRequestDTO provinceRequestDTO) throws Exception;
	
	void deleteProvince(String provinceId) throws Exception, ResourceNotFoundException;
	
	Province getProvince(String provinceId) throws Exception, ResourceNotFoundException;
	
	PagedResponseDTO<Province> getProvinces(int page, int limit, String sortField, String sortOder) throws Exception;
	
}
