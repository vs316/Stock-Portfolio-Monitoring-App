package test.java.com.prod.stockmonitor.stock_portfolio_monitor;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.PortfolioRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.StockHoldingRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.service.UserService;
import com.prod.stockmonitor.stock_portfolio_monitor.service.PortfolioService;
import com.prod.stockmonitor.stock_portfolio_monitor.service.StockHoldingService;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.GainLossDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockHoldingServiceTest {

    @Autowired
    private UserService s1;

    @Autowired
    private PortfolioService s2;

    @Autowired
    private StockHoldingService s3;

    @Test
    void testAddAndCalculateStock() {
        UserClass user = new UserClass();
        user.setUsername("user1");
        user.setEmailId("user1@example.com");
        user.setFullName("User1");
        user.setPassword("pass1");
        user.setRole("user");

        UserClass savedUser = s1.registerUser(user);

        PortfolioRequest preq = new PortfolioRequest();
        preq.setPortfolioName("Stock Portfolio");
        preq.setUserId(savedUser.getId());
        s2.savePortfolio(preq);

        List<Portfolio> portfolios = s2.getAllPortfolios();
        Portfolio portfolio = portfolios.get(portfolios.size() - 1);

        StockHoldingRequest stockReq = new StockHoldingRequest();
        stockReq.setPortfolioId(portfolio.getId()); // Not getPortfolioId()
        stockReq.setStockName("Google");
        stockReq.setStockSymbol("GOGLE");
        stockReq.setBuyPrice(100.0);
        stockReq.setCurrentPrice(150.0);
        stockReq.setQuantity(10.0);

        String result = s3.addStock(stockReq);
        assertEquals("Stock added!", result);

        List<GainLossDTO> gainLoss = s3.calculateGainLossForPortfolio(portfolio.getId());
        assertFalse(gainLoss.isEmpty());

        double totalGainLoss = s3.getTotalPortfolioGainLoss(portfolio.getId());
        assertEquals(500.0, totalGainLoss);
    }
}

