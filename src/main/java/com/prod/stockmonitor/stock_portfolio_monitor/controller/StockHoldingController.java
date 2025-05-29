package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.GainLossDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.StockHoldingRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.StockHolding;
import com.prod.stockmonitor.stock_portfolio_monitor.service.StockHoldingService;
import com.prod.stockmonitor.stock_portfolio_monitor.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockHoldingController {

    private final StockHoldingService stockService;
    private final UserService userService;

    public StockHoldingController(StockHoldingService stockService, UserService userService) {
        this.stockService = stockService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addStock(@RequestBody StockHoldingRequest req) {
        return ResponseEntity.ok(stockService.addStock(req));
    }

    @PutMapping("/update/{stockId}")
    public ResponseEntity<String> updateStock(@PathVariable Long stockId, @RequestBody StockHoldingRequest req) {
        return ResponseEntity.ok(stockService.updateStock(stockId, req));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/portfolio/{id}")
    public ResponseEntity<List<StockHolding>> getStocksForPortfolio(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getAllStocksForPortfolio(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<StockHolding>> getAllStocks(@RequestParam String userRole) {
        if ("admin".equalsIgnoreCase(userRole)) {
            return ResponseEntity.ok(stockService.getAllStocks());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("gainloss/{portfolioId}")
    public ResponseEntity<List<GainLossDTO>> gainLossPerStock(@PathVariable Long portfolioId){
        return ResponseEntity.ok(stockService.calculateGainLossForPortfolio(portfolioId));
    }
    @GetMapping("/gainloss/total/{portfolioId}")
    public ResponseEntity<Double> getTotalGainLoss(@PathVariable Long portfolioId){
        return ResponseEntity.ok(stockService.getTotalPortfolioGainLoss(portfolioId));
    }

    @PutMapping("/gainloss/update/{portfolioId}")
    public ResponseEntity<List<GainLossDTO>> updateGainLossForPortfolio(@PathVariable Long portfolioId) {
        List<GainLossDTO> updatedGainLossList = stockService.updateGainLossForPortfolio(portfolioId);
        return ResponseEntity.ok(updatedGainLossList);
    }

}
