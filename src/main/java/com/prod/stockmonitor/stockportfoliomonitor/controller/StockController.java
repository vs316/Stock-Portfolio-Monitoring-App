package com.prod.stockmonitor.stockportfoliomonitor.controller;

import com.prod.stockmonitor.stockportfoliomonitor.model.StockData;
import com.prod.stockmonitor.stockportfoliomonitor.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{name}")
    public StockData getStock(@PathVariable String name) {
        return stockService.getStockData(name);
    }
}
