package com.example.demo.service;

import com.example.demo.dto.PagedResponseDTO;
import com.example.demo.dto.PublisherRequestDTO;
import com.example.demo.dto.PublisherRequestUpdateDTO;
import com.example.demo.model.Publisher;

public interface PublisherService {
	
	public Publisher createPublisher(PublisherRequestDTO publisherRequestDTO);

	public Publisher getPublisher(String publisherId);

	public Publisher updatePublisher(String publisherId, PublisherRequestUpdateDTO publisherRequestUpdateDTO);

	public void deletePublisher(String publisherId);
	
	public PagedResponseDTO<Publisher> getAllPublishers(int page, int limit, String sortField, String sortOder);
	
}
