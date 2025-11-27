package com.Encora.capstone;

import com.Encora.capstone.controller.StockController;
import com.Encora.capstone.model.StockRecord;
import com.Encora.capstone.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private StockRecord sampleStock;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(stockController).build();
        objectMapper = new ObjectMapper();
        sampleStock = new StockRecord(); // Assuming StockRecord has a no-arg constructor; adjust fields as needed
        sampleStock.setStockId("TEST-STOCK-1");
        sampleStock.setUserId("TEST-USER-1");
        // Set other fields as per model definition
    }

    @Test
    void createStock_Success() throws Exception {
        when(stockService.addStock(any(StockRecord.class))).thenReturn(sampleStock);

        mockMvc.perform(post("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value("TEST-STOCK-1"));

        verify(stockService, times(1)).addStock(any(StockRecord.class));
    }

    @Test
    void updateStock_Success() throws Exception {
        when(stockService.updateStock(anyString(), any(StockRecord.class))).thenReturn(sampleStock);

        mockMvc.perform(put("/api/stocks/TEST-STOCK-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value("TEST-STOCK-1"));

        verify(stockService, times(1)).updateStock(eq("TEST-STOCK-1"), any(StockRecord.class));
    }

    @Test
    void deleteStock_Success() throws Exception {
        doNothing().when(stockService).deleteStock(anyString());

        mockMvc.perform(delete("/api/stocks/TEST-STOCK-1"))
                .andExpect(status().isNoContent());

        verify(stockService, times(1)).deleteStock("TEST-STOCK-1");
    }

    @Test
    void getUserStocks_Success() throws Exception {
        List<StockRecord> stockList = Collections.singletonList(sampleStock);
        when(stockService.listByUser(anyString())).thenReturn(stockList);

        mockMvc.perform(get("/api/stocks/user/TEST-USER-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockId").value("TEST-STOCK-1"));

        verify(stockService, times(1)).listByUser("TEST-USER-1");
    }

    @Test
    void getStockById_Success() throws Exception {
        when(stockService.getById(anyString())).thenReturn(Optional.of(sampleStock));

        mockMvc.perform(get("/api/stocks/TEST-STOCK-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockId").value("TEST-STOCK-1"));

        verify(stockService, times(1)).getById("TEST-STOCK-1");
    }

    @Test
    void getStockById_NotFound() throws Exception {
        when(stockService.getById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stocks/NON-EXISTENT-STOCK"))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).getById("NON-EXISTENT-STOCK");
    }

    @Test
    void getPortfolioSummary_Success() throws Exception {
        // Assuming StockService.PortfolioSummary is a simple class; mock it accordingly
        StockService.PortfolioSummary summary = mock(StockService.PortfolioSummary.class);
        when(stockService.calculatePortfolio(anyString())).thenReturn(summary);

        mockMvc.perform(get("/api/stocks/user/TEST-USER-1/summary"))
                .andExpect(status().isOk());

        verify(stockService, times(1)).calculatePortfolio("TEST-USER-1");
    }
}