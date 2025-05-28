package com.prod.stockmonitor.stock_portfolio_monitor.DTO;

import lombok.Data;

@Data
public class StockHoldingRequest {
    private Long portfolioId;
    private String stockSymbol;
    private String stockName;
    private Double quantity;
    private Double buyPrice;
    private Double currentPrice;
}

