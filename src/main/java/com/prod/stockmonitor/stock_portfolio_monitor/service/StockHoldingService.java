package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.GainLossDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.StockHoldingRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.model.StockHolding;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.PortfolioRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.StockHoldingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        stock.setCurrentPrice(req.getCurrentPrice()!=null ? req.getCurrentPrice():0.0);

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
        if (req.getCurrentPrice() != null) stock.setCurrentPrice(req.getCurrentPrice());

        stockRepo.save(stock);

        // Trigger gain/loss update after stock price update
        updateGainLossForStock(stock);

        return "Stock updated!";
    }

    private void updateGainLossForStock(StockHolding stock) {
        double buyPrice = stock.getBuyPrice() != null ? stock.getBuyPrice() : 0.0;
        double currentPrice = stock.getCurrentPrice() != null ? stock.getCurrentPrice() : 0.0;
        double quantity = stock.getQuantity() != null ? stock.getQuantity() : 0.0;

        double gainLoss = (currentPrice - buyPrice) * quantity;
        double percentage = buyPrice != 0 ? (gainLoss / (buyPrice * quantity)) * 100 : 0.0;

        stock.setGainLoss(gainLoss);
        stock.setPercentage(percentage);

        stockRepo.save(stock);
    }

    public List<GainLossDTO> calculateGainLossForPortfolio(Long portfolioId){
        List<StockHolding> stocks=stockRepo.findByPortfolioId(portfolioId);
        List<GainLossDTO> gainlossList=new ArrayList<>();
        for(StockHolding stock:stocks){
            double buyPrice=stock.getBuyPrice();
            double currentPrice= stock.getCurrentPrice()!=null?stock.getCurrentPrice():0.0;
            double quantity=stock.getQuantity()!=null?stock.getQuantity():0.0;
            double gainLoss=(currentPrice-buyPrice)*quantity;
            double percentage=buyPrice!=0?(gainLoss/(buyPrice*quantity))*100:0.0;
            GainLossDTO dto=new GainLossDTO();
            dto.setStockSymbol(stock.getStockSymbol());
            dto.setStockName(stock.getStockName());
            dto.setQuantity(quantity);
            dto.setCurrentPrice(currentPrice);
            dto.setBuyPrice(buyPrice);
            dto.setAbsoluteGainLoss(gainLoss);
            dto.setPercentageGainLoss(percentage);
            gainlossList.add(dto);


        }
    return gainlossList;
    }

    public double getTotalPortfolioGainLoss (Long portfolioId){
        return stockRepo.findByPortfolioId(portfolioId).stream().mapToDouble(stock ->{
            double currentPrice = stock.getCurrentPrice() != null ? stock.getCurrentPrice() : 0.0;
            double buyPrice = stock.getBuyPrice() != null ? stock.getBuyPrice() : 0.0;
            double quantity = stock.getQuantity() != null ? stock.getQuantity() : 0.0;
            return (currentPrice - buyPrice) * quantity;
        }).sum();
    }

    public List<GainLossDTO> updateGainLossForPortfolio(Long portfolioId) {
        List<StockHolding> stocks = stockRepo.findByPortfolioId(portfolioId);
        List<GainLossDTO> gainLossList = new ArrayList<>();

        for (StockHolding stock : stocks) {
            double buyPrice = stock.getBuyPrice() != null ? stock.getBuyPrice() : 0.0;
            double currentPrice = stock.getCurrentPrice() != null ? stock.getCurrentPrice() : 0.0;
            double quantity = stock.getQuantity() != null ? stock.getQuantity() : 0.0;
            double gainLoss = (currentPrice - buyPrice) * quantity;
            double percentage = buyPrice != 0 ? (gainLoss / (buyPrice * quantity)) * 100 : 0.0;

            // **Update stock entity in DB**
            stock.setGainLoss(gainLoss);
            stock.setPercentage(percentage);
            stockRepo.save(stock);  // Persist changes

            // **Prepare DTO response**
            GainLossDTO dto = new GainLossDTO();
            dto.setStockSymbol(stock.getStockSymbol());
            dto.setStockName(stock.getStockName());
            dto.setQuantity(quantity);
            dto.setCurrentPrice(currentPrice);
            dto.setBuyPrice(buyPrice);
            dto.setAbsoluteGainLoss(gainLoss);
            dto.setPercentageGainLoss(percentage);

            gainLossList.add(dto);
        }

        return gainLossList;
    }
}
