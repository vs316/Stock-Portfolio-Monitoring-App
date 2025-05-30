package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.prod.stockmonitor.stock_portfolio_monitor.model.Alert;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.AlertRepository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private JavaMailSender mailSender;



    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 6000000) // Check every 100 minutes
    @Transactional
    public void checkAlerts() {
        List<Alert> alerts = alertRepository.findAll();

        for (Alert alert : alerts) {
            try {
                Double currentPrice = fetchCurrentPrice(alert.getStockSymbol());
                if (currentPrice != null && currentPrice >= alert.getThresholdPrice()) {
                    sendEmail(alert.getEmail(), alert.getStockSymbol(), currentPrice, alert.getThresholdPrice());
                    alertRepository.delete(alert);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Double fetchCurrentPrice(String stockSymbol) throws IOException, InterruptedException {
        String url = "https://stock.indianapi.in/stock?name=" + stockSymbol;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Api-Key", "sk-live-73gVcZCMddsGRqWBUz5Rz0FruELydNjwzrFbNUz3")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonNode = objectMapper.readTree(response.body());

        if (jsonNode.has("currentPrice") && jsonNode.get("currentPrice").has("BSE")) {
            return jsonNode.get("currentPrice").get("BSE").asDouble();
        }

        return null;
    }

    private void sendEmail(String to, String stockSymbol, Double currentPrice, Double thresholdPrice) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Stock Alert: " + stockSymbol);
        message.setText("The current price of " + stockSymbol + " is " + currentPrice +
                ", which has exceeded your threshold of " + thresholdPrice + ".");
        mailSender.send(message);
    }
}

