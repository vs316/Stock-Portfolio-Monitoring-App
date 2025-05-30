package test.java.com.prod.stockmonitor.stock_portfolio_monitor;

import com.prod.stockmonitor.stock_portfolio_monitor.model.Alert;
import com.prod.stockmonitor.stock_portfolio_monitor.service.AlertService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AlertServiceTest {

    static class TestableAlertService extends AlertService {
        private boolean emailSent = false;
        private List<String> emails = new ArrayList<>();

        @Override
        public void checkAlerts() {
            Alert alert = new Alert();
            alert.setEmail("animishr1975@gmail.com");
            alert.setStockSymbol("FAKE");
            alert.setThresholdPrice(100.0);

            Double mockPrice = 120.0;
            if (mockPrice >= alert.getThresholdPrice()) {
                sendEmail(alert.getEmail(), alert.getStockSymbol(), mockPrice, alert.getThresholdPrice());
            }
        }


        protected Double fetchCurrentPrice(String stockSymbol) {
            return 120.0;
        }

        protected void sendEmail(String to, String stockSymbol, Double currentPrice, Double thresholdPrice) {
            emailSent = true;
            emails.add(to);
        }

        public boolean isEmailSent() {
            return emailSent;
        }

        public List<String> getEmails() {
            return emails;
        }
    }

    @Test
    void testCheckAlertsEmailSent() {
        TestableAlertService service = new TestableAlertService();
        service.checkAlerts();
        Assertions.assertTrue(service.isEmailSent());
        Assertions.assertEquals("animishr1975@gmail.com", service.getEmails().get(0));
    }
}

