package com.Encora.capstone.controller;

import com.Encora.capstone.model.StockRecord;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Encora.capstone.model.StockRecord;
import com.Encora.capstone.service.StockService;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/stocks")
@Validated
public class StockController {
private final StockService stockService;


public StockController(StockService stockService) {
this.stockService = stockService;
}


@PostMapping
public ResponseEntity<StockRecord> createStock(@Valid @RequestBody StockRecord request) {
StockRecord created = stockService.addStock(request);
return ResponseEntity.ok(created);
}


@PutMapping("/{stockId}")
public ResponseEntity<StockRecord> updateStock(@PathVariable String stockId, @RequestBody StockRecord update) {
StockRecord updated = stockService.updateStock(stockId, update);
return ResponseEntity.ok(updated);
}


@DeleteMapping("/{stockId}")
public ResponseEntity<Void> deleteStock(@PathVariable String stockId) {
stockService.deleteStock(stockId);
return ResponseEntity.noContent().build();
}


@GetMapping("/user/{userId}")
public ResponseEntity<List<StockRecord>> getUserStocks(@PathVariable @jakarta.validation.constraints.NotBlank String userId) {
return ResponseEntity.ok(stockService.listByUser(userId));
}


@GetMapping("/{stockId}")
public ResponseEntity<StockRecord> getStockById(@PathVariable String stockId) {
return stockService.getById(stockId)
.map(ResponseEntity::ok)
.orElseGet(() -> ResponseEntity.notFound().build());
}


@GetMapping("/user/{userId}/summary")
public ResponseEntity<?> getPortfolioSummary(@PathVariable String userId) {
StockService.PortfolioSummary summary = stockService.calculatePortfolio(userId);
return ResponseEntity.ok(summary);
}
}