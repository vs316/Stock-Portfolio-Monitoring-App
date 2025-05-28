package com.prod.stockmonitor.stock_portfolio_monitor.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GainLossDTO {
    private String stockSymbol;
    private String stockName;
    private Double buyPrice;
    private Double currentPrice;
    private Double quantity;
    private Double absoluteGainLoss;
    private Double percentageGainLoss;
}
