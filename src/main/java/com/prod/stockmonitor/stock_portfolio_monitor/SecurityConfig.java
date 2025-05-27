package com.prod.stockmonitor.stock_portfolio_monitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/register", "/login", "/api/user/**").permitAll() // Allow public access
//                        .anyRequest().authenticated()
//                );

        return http.build();
    }
}