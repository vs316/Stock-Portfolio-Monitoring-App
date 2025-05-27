package com.prod.stockmonitor.stockportfoliomonitor.service;

import com.prod.stockmonitor.stockportfoliomonitor.model.StockData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    @Value("${stock.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://api.twelvedata.com/time_series";

    public StockData getStockData(String symbol) {
        String url = buildApiUrl(symbol);

        RestTemplate restTemplate = new RestTemplate();
        String response;

        try {
            response = restTemplate.getForObject(url, String.class);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to connect to stock API: " + ex.getMessage());
        }

        try {
            JSONObject json = new JSONObject(response);

            if (json.has("status") && json.getString("status").equals("error")) {
                throw new RuntimeException("API Error: " + json.getString("message"));
            }

            if (!json.has("meta") || !json.has("values")) {
                throw new RuntimeException("Unexpected API response format.");
            }

            JSONObject meta = json.getJSONObject("meta");
            JSONObject latestData = json.getJSONArray("values").getJSONObject(0);

            StockData data = new StockData();
            data.setSymbol(meta.optString("symbol", "N/A"));
            data.setDatetime(latestData.optString("datetime", "N/A"));
            data.setPrice(latestData.optString("close", "0"));

            return data;

        } catch (Exception ex) {
            throw new RuntimeException("Error parsing stock data: " + ex.getMessage());
        }
    }

    // Optional: method for multi-symbol support
    public List<StockData> getMultipleStockData(List<String> symbols) {
        List<StockData> results = new ArrayList<>();
        for (String symbol : symbols) {
            try {
                results.add(getStockData(symbol));
            } catch (RuntimeException e) {
                // log or ignore specific errors per symbol
                System.out.println("Error fetching symbol " + symbol + ": " + e.getMessage());
            }
        }
        return results;
    }

    private String buildApiUrl(String symbol) {
        return UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("symbol", symbol)
                .queryParam("interval", "1min")
                .queryParam("apikey", apiKey)
                .toUriString();
    }
}
