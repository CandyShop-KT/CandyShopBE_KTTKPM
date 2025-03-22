package com.example.demo.service.imp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.PublisherRequestDTO;
import com.example.demo.dto.PublisherRequestUpdateDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Publisher;
import com.example.demo.repository.PublisherRepository;
import com.example.demo.service.PublisherService;

@Service
public class PublisherServiceImp implements PublisherService{
	
	private PublisherRepository publisherRepository;
	
	public PublisherServiceImp(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}

	@Override
	@Transactional
	public Publisher createPublisher(PublisherRequestDTO publisherRequestDTO) {
		if (publisherRepository.existsByPublisherNameIgnoreCase(publisherRequestDTO.getPublisherName()))
			throw new ResourceConflictException("publisherName", "Publisher name already exists");
		if (publisherRepository.existsByPhoneNumber(publisherRequestDTO.getPhoneNumber()))
			throw new ResourceConflictException("phoneNumber", "Phone number already exists");
		if (publisherRepository.existsByEmail(publisherRequestDTO.getEmail()))
			throw new ResourceConflictException("email", "Email already exists");
		if(publisherRepository.existsByAddressIgnoreCase(publisherRequestDTO.getAddress()))
            throw new ResourceConflictException("address", "Address already exists");
	    Publisher publisher = new Publisher();
	    publisher.setPublisherName(publisherRequestDTO.getPublisherName());
	    publisher.setPhoneNumber(publisherRequestDTO.getPhoneNumber());
	    publisher.setAddress(publisherRequestDTO.getAddress());
	    publisher.setEmail(publisherRequestDTO.getEmail());
	    return publisherRepository.save(publisher);
	}

	@Override
	public Publisher getPublisher(String publisherId) {
		return publisherRepository.findById(publisherId)
				.orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
	}

	@Override
	@Transactional
	public Publisher updatePublisher(String publisherId, PublisherRequestUpdateDTO publisherRequestUpdateDTO) {
		Publisher publisher = publisherRepository.findById(publisherId)
				.orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
		if (publisherRepository.existsByPhoneNumber(publisherRequestUpdateDTO.getPhoneNumber()))
			throw new ResourceConflictException("phoneNumber", "Phone number already exists");
		if (publisherRepository.existsByEmail(publisherRequestUpdateDTO.getEmail()))
			throw new ResourceConflictException("email", "Email already exists");
		if (publisherRequestUpdateDTO.getPublisherName() != null)
			publisher.setPublisherName(publisherRequestUpdateDTO.getPublisherName());
		if (publisherRequestUpdateDTO.getPhoneNumber() != null)
			publisher.setPhoneNumber(publisherRequestUpdateDTO.getPhoneNumber());
		if (publisherRequestUpdateDTO.getAddress() != null)
			publisher.setAddress(publisherRequestUpdateDTO.getAddress());
		if (publisherRequestUpdateDTO.getEmail() != null)
			publisher.setEmail(publisherRequestUpdateDTO.getEmail());
		return publisherRepository.save(publisher);
	}

	@Override
	@Transactional
	public void deletePublisher(String publisherId) {
		if (!publisherRepository.existsById(publisherId))
			throw new ResourceNotFoundException("Publisher not found");
		publisherRepository.deleteById(publisherId);
	}

	@Override
	public PagedResponseDTO<Publisher> getAllPublishers(int page, int limit, String sortField, String sortOder) {
		Sort sort = sortOder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<Publisher> publishers = publisherRepository.findAll(pageable);
		PagedResponseDTO<Publisher> response = new PagedResponseDTO<>();
		response.setContent(publishers.getContent());
		response.setPageNumber(publishers.getNumber());
		response.setPageSize(publishers.getSize());
		response.setTotalElements(publishers.getTotalElements());
		response.setTotalPages(publishers.getTotalPages());
		return response;
	}

}
