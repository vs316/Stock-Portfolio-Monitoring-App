package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Alert;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.AlertRepository;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;

    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        alert.setCreatedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }
}
