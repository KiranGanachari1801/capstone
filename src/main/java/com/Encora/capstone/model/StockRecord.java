package com.Encora.capstone.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


import java.time.Instant;


@DynamoDbBean
public class StockRecord {
private String stockId; // PK
private String userId; // user
private String symbol;
private String companyName;
private Integer quantity;
private Double purchasePrice;
private Double currentPrice;
private String purchaseDate;
private Double totalInvestment;
private Double currentValue;
private Double gainLoss;
private Long createdAt;


@DynamoDbPartitionKey
public String getStockId() { return stockId; }
public void setStockId(String stockId) { this.stockId = stockId; }


public String getUserId() { return userId; }
public void setUserId(String userId) { this.userId = userId; }


public String getSymbol() { return symbol; }
public void setSymbol(String symbol) { this.symbol = symbol; }


public String getCompanyName() { return companyName; }
public void setCompanyName(String companyName) { this.companyName = companyName; }


public Integer getQuantity() { return quantity; }
public void setQuantity(Integer quantity) { this.quantity = quantity; }


public Double getPurchasePrice() { return purchasePrice; }
public void setPurchasePrice(Double purchasePrice) { this.purchasePrice = purchasePrice; }


public Double getCurrentPrice() { return currentPrice; }
public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }


public String getPurchaseDate() { return purchaseDate; }
public void setPurchaseDate(String purchaseDate) { this.purchaseDate = purchaseDate; }


public Double getTotalInvestment() { return totalInvestment; }
public void setTotalInvestment(Double totalInvestment) { this.totalInvestment = totalInvestment; }


public Double getCurrentValue() { return currentValue; }
public void setCurrentValue(Double currentValue) { this.currentValue = currentValue; }


public Double getGainLoss() { return gainLoss; }
public void setGainLoss(Double gainLoss) { this.gainLoss = gainLoss; }


public Long getCreatedAt() { return createdAt; }
public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }
}