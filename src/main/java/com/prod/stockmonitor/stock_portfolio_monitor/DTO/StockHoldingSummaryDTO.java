package com.prod.stockmonitor.stock_portfolio_monitor.DTO;


public record StockHoldingSummaryDTO(
        Long holdingId,
        String stockSymbol,
        String stockName,
        Double quantity,
        Double buyPrice,
        Double currentPrice,
        Double gainLoss,
        Double gainLossPercentage
) {
    @Override
    public Long holdingId() {
        return holdingId;
    }

    @Override
    public String stockSymbol() {
        return stockSymbol;
    }

    @Override
    public String stockName() {
        return stockName;
    }

    @Override
    public Double quantity() {
        return quantity;
    }

    @Override
    public Double buyPrice() {
        return buyPrice;
    }

    @Override
    public Double currentPrice() {
        return currentPrice;
    }

    @Override
    public Double gainLoss() {
        return gainLoss;
    }

    @Override
    public Double gainLossPercentage() {
        return gainLossPercentage;
    }
}