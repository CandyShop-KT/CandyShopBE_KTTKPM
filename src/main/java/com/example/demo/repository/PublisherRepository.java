package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, String>{
	
	boolean existsByPublisherNameIgnoreCase(String publisherName);
	
	boolean existsByPhoneNumber(String phoneNumber);
	
	boolean existsByEmail(String email);
	
	boolean existsByAddressIgnoreCase(String address);
	
}
