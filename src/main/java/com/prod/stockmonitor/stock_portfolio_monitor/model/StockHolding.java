package com.prod.stockmonitor.stock_portfolio_monitor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_holdings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockHolding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String stockSymbol;

    private String stockName;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double buyPrice;

    private Double currentPrice;  // For real-time updates (optional)

    private Double gainLoss;      // Calculated based on current price (optional)
}
