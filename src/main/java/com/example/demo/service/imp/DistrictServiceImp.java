package com.example.demo.service.imp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.DistrictRequestDTO;
import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.District;
import com.example.demo.model.Province;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.ProvinceRepository;
import com.example.demo.service.DistrictService;

@Service
public class DistrictServiceImp implements DistrictService {

	private DistrictRepository districtRepository;
	private ProvinceRepository provinceRepository;

	public DistrictServiceImp(DistrictRepository districtRepository, ProvinceRepository provinceRepository) {
		this.districtRepository = districtRepository;
		this.provinceRepository = provinceRepository;
	}

	@Override
	@Transactional
	public District createDistrict(String provinceId, DistrictRequestDTO districtRequestDTO)
			throws Exception, ResourceConflictException, ResourceNotFoundException {
		Province province = provinceRepository.findById(provinceId)
				.orElseThrow(() -> new ResourceNotFoundException("Province not found"));
		if (districtRepository.existsByDistrictName(districtRequestDTO.getDistrictName()))
			throw new ResourceConflictException("districtName", "District name already exists");
		District district = new District();
		district.setDistrictName(districtRequestDTO.getDistrictName());
		district.setProvince(province);
		return districtRepository.save(district);
	}

	@Override
	@Transactional
	public District updateDistrict(String districtId, DistrictRequestDTO districtRequestDTO)
			throws Exception, ResourceNotFoundException {
		District district = districtRepository.findById(districtId)
				.orElseThrow(() -> new ResourceNotFoundException("District not found"));
		if (district.getDistrictName().equals(districtRequestDTO.getDistrictName()))
			throw new ResourceConflictException("districtName", "No changes were made to the district name");
		district.setDistrictName(districtRequestDTO.getDistrictName());
		return districtRepository.save(district);
	}

	@Override
	@Transactional
	public void deleteDistrict(String districtId) throws Exception, ResourceNotFoundException {
		District district = districtRepository.findById(districtId)
				.orElseThrow(() -> new ResourceNotFoundException("District not found"));
		districtRepository.delete(district);
	}

	@Override
	public District getDistrict(String districtId) throws Exception, ResourceNotFoundException {
		return districtRepository.findById(districtId)
				.orElseThrow(() -> new ResourceNotFoundException("District not found"));
	}

	@Override
	public PagedResponseDTO<District> getDistrictsByProvinceId(String provinceId, int page, int limit, String sortField,
			String sortOder) throws Exception {
		Sort sort = sortOder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending()
				: Sort.by(sortField).ascending();
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<District> pageProvince = districtRepository.findByProvinceProvinceId(provinceId, pageable);
		return new PagedResponseDTO<District>(pageProvince.getContent(), pageProvince.getNumber(),
				pageProvince.getSize(), pageProvince.getTotalElements(), pageProvince.getTotalPages());
	}

}
