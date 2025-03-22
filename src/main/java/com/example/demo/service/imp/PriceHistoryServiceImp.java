package com.example.demo.service.imp;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PriceHistoryRequestDTO;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PriceHistory;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.service.PriceHistoryService;

import jakarta.transaction.Transactional;

@Service
public class PriceHistoryServiceImp implements PriceHistoryService {

	private PriceHistoryRepository priceHistoryRepository;

	public PriceHistoryServiceImp(PriceHistoryRepository priceHistoryRepository) {
		this.priceHistoryRepository = priceHistoryRepository;
	}

	@Override
	@Transactional
	public PriceHistory updatePriceHistory(String priceHistoryId, PriceHistoryRequestDTO priceHistoryRequestDTO) {
		PriceHistory priceHistory = priceHistoryRepository.findById(priceHistoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Price history not found"));
		if (priceHistory.getPriceChangeEffectiveDate().isBefore(LocalDateTime.now()))
			throw new ResourceConflictException("priceChangeEffectiveDate", "Cannot update price history with effective date before the current");
		if (priceHistoryRequestDTO.getNewPrice() != 0)
			priceHistory.setNewPrice(priceHistoryRequestDTO.getNewPrice());
		if (priceHistoryRequestDTO.getPriceChangeReason() != null)
			priceHistory.setPriceChangeReason(priceHistoryRequestDTO.getPriceChangeReason());
		if (priceHistoryRequestDTO.getPriceChangeEffectiveDate() != null)
			priceHistory.setPriceChangeEffectiveDate(priceHistoryRequestDTO.getPriceChangeEffectiveDate());
		return priceHistoryRepository.save(priceHistory);
	}

	@Override
	public PriceHistory getPriceHistory(String id) {
		PriceHistory priceHistory = priceHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Price history not found"));
		return priceHistory;
	}

	@Override
	@Transactional
	public void deletePriceHistory(String id) {
		PriceHistory priceHistory = priceHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Price history not found"));
		if (priceHistory.getPriceChangeEffectiveDate().isBefore(LocalDateTime.now()))
			throw new ResourceConflictException("priceChangeEffectiveDate", "Cannot delete price history with effective date before the current");
		priceHistoryRepository.delete(priceHistory);
	}

}
