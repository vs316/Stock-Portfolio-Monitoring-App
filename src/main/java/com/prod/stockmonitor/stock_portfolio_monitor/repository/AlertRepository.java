package com.prod.stockmonitor.stock_portfolio_monitor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
