package com.prod.stockmonitor.stock_portfolio_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockPortfolioMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPortfolioMonitorApplication.class, args);
	}

}
