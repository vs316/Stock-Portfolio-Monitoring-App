package com.prod.stockmonitor.stock_portfolio_monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module; // For Hibernate 6
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // This ensures lazy-loaded fields are skipped (or you can customize behavior)
        mapper.registerModule(new Hibernate6Module()); // Use Hibernate6Module for Hibernate 6
        return mapper;
    }
}