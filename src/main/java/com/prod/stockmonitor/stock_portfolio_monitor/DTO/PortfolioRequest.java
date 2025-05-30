package com.prod.stockmonitor.stock_portfolio_monitor.DTO;

import lombok.Data;

@Data
public class PortfolioRequest {
    private Long userId;
    private String portfolioName;
}
