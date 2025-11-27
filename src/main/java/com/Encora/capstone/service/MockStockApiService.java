package com.Encora.capstone.service;

import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class MockStockApiService {
// simple in-memory mock: symbol -> (companyName, basePrice)
private final Map<String, Double> priceBase = new HashMap<>();
private final Map<String, String> companyMap = new HashMap<>();
private final Random rnd = new Random();


//MockStockApiService.java (constructor)
public MockStockApiService() {
    companyMap.put("AAPL", "Apple Inc.");   priceBase.put("AAPL", 170.0);
    companyMap.put("GOOG", "Alphabet Inc.");priceBase.put("GOOG", 135.0);
    companyMap.put("MSFT", "Microsoft Corporation"); priceBase.put("MSFT", 320.0);
    companyMap.put("TSLA", "Tesla, Inc.");  priceBase.put("TSLA", 220.0);

    // Indian demo tickers
    companyMap.put("RELIANCE", "Reliance Industries Limited"); priceBase.put("RELIANCE", 2700.0);
    companyMap.put("TCS", "Tata Consultancy Services Ltd."); priceBase.put("TCS", 3200.0);
    companyMap.put("INFY", "Infosys Limited"); priceBase.put("INFY", 1500.0);
}


public boolean isValidSymbol(String symbol) {
if (symbol == null) return false;
return companyMap.containsKey(symbol.toUpperCase());
}


public String getCompanyName(String symbol) {
return companyMap.get(symbol.toUpperCase());
}


public double fetchCurrentPrice(String symbol) {
Double base = priceBase.get(symbol.toUpperCase());
if (base == null) return 0.0;
// simulate small random fluctuation
double changePercent = (rnd.nextDouble() - 0.5) * 0.1; // +/-5%
return Math.round(base * (1 + changePercent) * 100.0) / 100.0;
}
}