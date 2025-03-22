package com.example.demo.service;

import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.WardRequestDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Province;
import com.example.demo.model.Ward;

public interface WardService {
	
	Ward createWard(String districtId, WardRequestDTO wardRequestDTO) throws Exception, ResourceNotFoundException;

	Ward updateWard(String wardId, WardRequestDTO wardRequestDTO) throws Exception, ResourceNotFoundException;

	void deleteWard(String wardId) throws Exception, ResourceNotFoundException;

	Ward getWard(String wardId) throws Exception, ResourceNotFoundException;

	PagedResponseDTO<Ward> getWardsByDistrictId(String districtId, int page, int limit, String sortField, String sortOder) throws Exception;
	
}
