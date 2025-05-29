package com.prod.stockmonitor.stock_portfolio_monitor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module; // For Hibernate 6
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Import JavaTimeModule
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // This ensures lazy-loaded fields are skipped (or you can customize behavior)
        mapper.registerModule(new Hibernate6Module()); // Use Hibernate6Module for Hibernate 6

        // Register JavaTimeModule to support Java 8 Date and Time types (like LocalDate)
        mapper.registerModule(new JavaTimeModule());

        // Optional: To serialize dates as ISO-8601 strings instead of timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}