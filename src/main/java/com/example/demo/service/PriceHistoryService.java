package com.example.demo.service;

import com.example.demo.dto.PriceHistoryRequestDTO;
import com.example.demo.model.PriceHistory;

public interface PriceHistoryService {

	public PriceHistory updatePriceHistory(String priceHistoryId, PriceHistoryRequestDTO priceHistoryRequestDTO);

	public PriceHistory getPriceHistory(String id);

	public void deletePriceHistory(String id);

}
