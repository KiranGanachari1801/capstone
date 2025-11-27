package com.Encora.capstone.repository;

import com.Encora.capstone.model.StockRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StockRepository {
    private final DynamoDbTable<StockRecord> stockTable;

    public StockRepository(DynamoDbEnhancedClient enhancedClient,
                           @Value("${aws.dynamodb.tableName:user-stocks}") String tableName) {
        this.stockTable = enhancedClient.table(tableName, TableSchema.fromBean(StockRecord.class));
        try {
            // Optionally create table in dev; in production, manage table lifecycle separately
        } catch (ResourceNotFoundException ignored) {
        }
    }

    public void save(StockRecord record) {
        stockTable.putItem(record);
    }

    public Optional<StockRecord> findById(String stockId) {
        StockRecord rec = stockTable.getItem(r -> r.key(k -> k.partitionValue(stockId)));
        return Optional.ofNullable(rec);
    }

    public void deleteById(String stockId) {
        stockTable.deleteItem(r -> r.key(k -> k.partitionValue(stockId)));
    }

    public List<StockRecord> findByUserId(String userId) {
        List<StockRecord> results = new ArrayList<>();
        stockTable.scan().items().forEach(results::add);
        List<StockRecord> filtered = new ArrayList<>();
        for (StockRecord r : results) {
            if (r.getUserId() != null && r.getUserId().equals(userId)) filtered.add(r);
        }
        return filtered;
    }

    public List<StockRecord> findAll() {
        List<StockRecord> results = new ArrayList<>();
        stockTable.scan().items().forEach(results::add);
        return results;
    }
}
