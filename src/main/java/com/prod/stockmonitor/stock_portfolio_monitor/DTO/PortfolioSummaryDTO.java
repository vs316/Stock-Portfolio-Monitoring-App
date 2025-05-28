// PortfolioSummaryDTO.java
package com.prod.stockmonitor.stock_portfolio_monitor.DTO;

import java.time.LocalDate;
import java.util.List;

public record PortfolioSummaryDTO(
        Long portfolioId,
        String portfolioName,
        Long userId,
        String userName,
        Double totalInvestedValue,
        Double currentMarketValue,
        Double totalGainLoss,
        Double totalGainLossPercentage,
        LocalDate asOfDate,
        List<StockHoldingSummaryDTO> stockHoldings
) {}