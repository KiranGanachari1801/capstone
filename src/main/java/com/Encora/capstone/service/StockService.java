package com.Encora.capstone.service;

import com.Encora.capstone.model.StockRecord;
import com.Encora.capstone.repository.StockRepository;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {
	private final StockRepository repository;
	private final MockStockApiService mockApi;


	public StockService(StockRepository repository, MockStockApiService mockApi) {
	this.repository = repository;
	this.mockApi = mockApi;
	}
	public StockRecord addStock(StockRecord request) {
		// validate symbol
		if (!mockApi.isValidSymbol(request.getSymbol())) {
		throw new IllegalArgumentException("Invalid stock symbol: " + request.getSymbol());
		}
		String stockId = UUID.randomUUID().toString();
		request.setStockId(stockId);
		request.setCreatedAt(Instant.now().toEpochMilli());
		// fetch currentPrice from mock API
		double currentPrice = mockApi.fetchCurrentPrice(request.getSymbol());
		request.setCurrentPrice(currentPrice);
		request.setCompanyName(mockApi.getCompanyName(request.getSymbol()));
		// compute derived fields
		double totalInvestment = request.getQuantity() * request.getPurchasePrice();
		double currentValue = request.getQuantity() * currentPrice;
		double gainLoss = currentValue - totalInvestment;
		request.setTotalInvestment(totalInvestment);
		request.setCurrentValue(currentValue);
		request.setGainLoss(gainLoss);


		repository.save(request);
		return request;
		}
	public StockRecord updateStock(String stockId, StockRecord update) {
		Optional<StockRecord> existingOpt = repository.findById(stockId);
		if (existingOpt.isEmpty()) throw new IllegalArgumentException("Stock not found");
		StockRecord existing = existingOpt.get();
		// allow update of quantity, purchasePrice, purchaseDate only for demo
		if (update.getQuantity() != null) existing.setQuantity(update.getQuantity());
		if (update.getPurchasePrice() != null) existing.setPurchasePrice(update.getPurchasePrice());
		if (update.getPurchaseDate() != null) existing.setPurchaseDate(update.getPurchaseDate());
		// refresh current price and derived fields
		double currentPrice = mockApi.fetchCurrentPrice(existing.getSymbol());
		existing.setCurrentPrice(currentPrice);
		double totalInvestment = existing.getQuantity() * existing.getPurchasePrice();
		double currentValue = existing.getQuantity() * currentPrice;
		existing.setTotalInvestment(totalInvestment);
		existing.setCurrentValue(currentValue);
		existing.setGainLoss(currentValue - totalInvestment);


		repository.save(existing);
		return existing;
		}
	
	public void deleteStock(String stockId) {
		repository.deleteById(stockId);
		}


		public List<StockRecord> listByUser(String userId) {
		return repository.findByUserId(userId);
		}


		public Optional<StockRecord> getById(String stockId) {
		return repository.findById(stockId);
		}
		public PortfolioSummary calculatePortfolio(String userId) {
			List<StockRecord> holdings = listByUser(userId);
			double totalInvestment = 0.0;
			double currentValue = 0.0;
			for (StockRecord r : holdings) {
			// refresh current price
			double cp = mockApi.fetchCurrentPrice(r.getSymbol());
			r.setCurrentPrice(cp);
			r.setCurrentValue(cp * r.getQuantity());
			r.setTotalInvestment(r.getPurchasePrice() * r.getQuantity());
			r.setGainLoss(r.getCurrentValue() - r.getTotalInvestment());
			totalInvestment += r.getTotalInvestment();
			currentValue += r.getCurrentValue();
			// persist updated values (optional)
			repository.save(r);
			}
			double gainLoss = currentValue - totalInvestment;
			double pctChange = totalInvestment == 0 ? 0 : (gainLoss / totalInvestment) * 100.0;
			return new PortfolioSummary(totalInvestment, currentValue, gainLoss, pctChange, holdings);
			}
		public static class PortfolioSummary {
			public double totalInvestment;
			public double currentValue;
			public double gainLoss;
			public double percentageChange;
			public List<StockRecord> holdings;


			public PortfolioSummary(double totalInvestment, double currentValue, double gainLoss, double percentageChange, List<StockRecord> holdings) {
			this.totalInvestment = totalInvestment;
			this.currentValue = currentValue;
			this.gainLoss = gainLoss;
			this.percentageChange = percentageChange;
			this.holdings = holdings;
			}
			}

}
