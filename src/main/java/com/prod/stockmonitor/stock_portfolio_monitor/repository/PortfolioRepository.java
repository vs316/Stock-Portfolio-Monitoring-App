package com.prod.stockmonitor.stock_portfolio_monitor.repository;

import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    // Add custom queries if needed later
}
