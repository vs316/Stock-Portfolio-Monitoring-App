package com.prod.stockmonitor.stock_portfolio_monitor.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}