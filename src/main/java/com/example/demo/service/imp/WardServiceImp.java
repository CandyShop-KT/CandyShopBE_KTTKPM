package com.example.demo.service.imp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.WardRequestDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.District;
import com.example.demo.model.Ward;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.WardRepository;
import com.example.demo.service.WardService;

@Service
public class WardServiceImp implements WardService {

	private DistrictRepository districtRepository;
	private WardRepository wardRepository;
	
	public WardServiceImp(DistrictRepository districtRepository, WardRepository wardRepository) {
		this.districtRepository = districtRepository;
		this.wardRepository = wardRepository;
	}

	@Override
	@Transactional
	public Ward createWard(String districtId, WardRequestDTO wardRequestDTO)
			throws Exception, ResourceNotFoundException, ResourceConflictException {
		if (wardRepository.existsByWardName(wardRequestDTO.getWardName()))
			throw new ResourceConflictException("wardName", "Ward name is exist!");
		District district = districtRepository.findById(districtId)
				.orElseThrow(() -> new ResourceNotFoundException("Not found district"));
		Ward ward = new Ward();
		ward.setDistrict(district);
		ward.setWardName(wardRequestDTO.getWardName());
		Ward myWard = wardRepository.save(ward);
		return myWard;
	}

	@Override
	@Transactional
	public Ward updateWard(String wardId, WardRequestDTO wardRequestDTO)
			throws Exception, ResourceNotFoundException, ResourceConflictException {
		Ward ward = wardRepository.findById(wardId).orElseThrow(() -> new ResourceNotFoundException("Not found ward"));
		if (ward.getWardName().equals(wardRequestDTO.getWardName()))
			throw new ResourceConflictException("wardName", "No change in Ward Name!");
		ward.setWardName(wardRequestDTO.getWardName());
		Ward myWard = wardRepository.save(ward);
		return myWard;
	}

	@Override
	@Transactional
	public void deleteWard(String wardId) throws Exception, ResourceNotFoundException {
		Ward ward = wardRepository.findById(wardId).orElseThrow(() -> new ResourceNotFoundException("Not found ward"));
		wardRepository.delete(ward);
	}

	@Override
	public Ward getWard(String wardId) throws Exception, ResourceNotFoundException {
		Ward ward = wardRepository.findById(wardId).orElseThrow(() -> new ResourceNotFoundException("Not found ward"));
		return ward;
	}

	@Override
	public PagedResponseDTO<Ward> getWardsByDistrictId(String districtId, int page, int limit, String sortField,
			String sortOder) throws Exception {
		Sort sort = sortOder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending()
				: Sort.by(sortField).ascending();
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<Ward> wards = wardRepository.findByDistrictDistrictId(districtId, pageable);
		PagedResponseDTO<Ward> pagedResponseDTO = new PagedResponseDTO<>();
		pagedResponseDTO.setContent(wards.getContent());
		pagedResponseDTO.setTotalElements(wards.getTotalElements());
		pagedResponseDTO.setTotalPages(wards.getTotalPages());
		pagedResponseDTO.setPageSize(wards.getSize());
		pagedResponseDTO.setPageNumber(wards.getNumber());
		return pagedResponseDTO;
	}

}
