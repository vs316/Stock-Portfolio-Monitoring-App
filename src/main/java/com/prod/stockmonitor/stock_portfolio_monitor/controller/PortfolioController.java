package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.PortfolioRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping("/createPortfolio")
    public ResponseEntity<String> createPortfolio(@RequestBody PortfolioRequest request) {
        String message = portfolioService.savePortfolio(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> getPortfolio(@PathVariable Long portfolioId) {
        return portfolioService.getPortfolioById(portfolioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getAllPortfolios() {
        return ResponseEntity.ok(portfolioService.getAllPortfolios());
    }

    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long portfolioId) {
        portfolioService.deletePortfolio(portfolioId);
        return ResponseEntity.noContent().build();
    }
}
