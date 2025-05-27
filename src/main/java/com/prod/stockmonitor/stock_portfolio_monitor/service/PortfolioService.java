package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.PortfolioRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.PortfolioRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository; // Add this!

    public PortfolioService(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    public String savePortfolio(PortfolioRequest request) {
        // 1️⃣ Fetch the user by ID (ensure it's a managed entity)
        UserClass user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Create and set the portfolio
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setPortfolioName(request.getPortfolioName());

        // 3️⃣ Save
        portfolioRepository.save(portfolio);
        return "Portfolio saved successfully!";
    }

    public Optional<Portfolio> getPortfolioById(Long portfolioId) {
        return portfolioRepository.findById(portfolioId);
    }

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public void deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
    }
}

