package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.StockHoldingRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.model.StockHolding;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.PortfolioRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.StockHoldingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockHoldingService {

    private final StockHoldingRepository stockRepo;
    private final PortfolioRepository portfolioRepo;

    public StockHoldingService(StockHoldingRepository stockRepo, PortfolioRepository portfolioRepo) {
        this.stockRepo = stockRepo;
        this.portfolioRepo = portfolioRepo;
    }

    public String addStock(StockHoldingRequest req) {
        Portfolio portfolio = portfolioRepo.findById(req.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        StockHolding stock = new StockHolding();
        stock.setPortfolio(portfolio);
        stock.setStockSymbol(req.getStockSymbol());
        stock.setStockName(req.getStockName());
        stock.setQuantity(req.getQuantity());
        stock.setBuyPrice(req.getBuyPrice());

        stockRepo.save(stock);
        return "Stock added!";
    }

    public List<StockHolding> getAllStocksForPortfolio(Long portfolioId) {
        return stockRepo.findByPortfolioId(portfolioId);
    }

    public List<StockHolding> getAllStocks() {
        return stockRepo.findAll();
    }

    public void deleteStock(Long stockId) {
        stockRepo.deleteById(stockId);
    }

    public String updateStock(Long stockId, StockHoldingRequest req) {
        StockHolding stock = stockRepo.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (req.getStockSymbol() != null) stock.setStockSymbol(req.getStockSymbol());
        if (req.getStockName() != null) stock.setStockName(req.getStockName());
        if (req.getQuantity() != null) stock.setQuantity(req.getQuantity());
        if (req.getBuyPrice() != null) stock.setBuyPrice(req.getBuyPrice());

        stockRepo.save(stock);
        return "Stock updated!";
    }
}
