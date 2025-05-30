package test.java.com.prod.stockmonitor.stock_portfolio_monitor;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.PortfolioRequest;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.service.PortfolioService;
import com.prod.stockmonitor.stock_portfolio_monitor.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PortfolioServiceTest {

    @Autowired
    UserService s1;
    @Autowired
    PortfolioService s2;

    @Test
    void testSavePortfolio() {
        UserClass user = new UserClass();
        user.setUsername("gg");
        user.setEmailId("gg@gmail.com");
        user.setFullName("GG");
        user.setPassword("54414");
        user.setRole("user");
        UserClass savedUser = s1.registerUser(user);

        PortfolioRequest request = new PortfolioRequest();
        request.setUserId(savedUser.getId());
        request.setPortfolioName("demo2");
        String response = s2.savePortfolio(request);
        Assertions.assertEquals("Portfolio saved successfully!", response);

        List<Portfolio> all = s2.getAllPortfolios();
        Assertions.assertTrue(all.stream().anyMatch(p -> p.getPortfolioName().equals("demo2")));

    }


}
