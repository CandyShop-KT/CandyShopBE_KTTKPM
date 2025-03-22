package com.example.demo.service;

import com.example.demo.dto.DistrictRequestDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.District;

public interface DistrictService {
	
	District createDistrict(String provinceId, DistrictRequestDTO districtRequestDTO) throws Exception, ResourceConflictException;
	
	District updateDistrict(String districtId, DistrictRequestDTO districtRequestDTO) throws Exception;
	
	void deleteDistrict(String districtId) throws Exception, ResourceNotFoundException;
	
	District getDistrict(String districtId) throws Exception, ResourceNotFoundException;
	
	PagedResponseDTO<District> getDistrictsByProvinceId(String provinceId, int page, int limit, String sortField, String sortOder) throws Exception;
	
}
