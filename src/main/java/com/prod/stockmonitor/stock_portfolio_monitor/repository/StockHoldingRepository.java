package com.prod.stockmonitor.stock_portfolio_monitor.repository;


import com.prod.stockmonitor.stock_portfolio_monitor.model.StockHolding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockHoldingRepository extends JpaRepository<StockHolding, Long> {
    List<StockHolding> findByPortfolioId(Long id);
}
