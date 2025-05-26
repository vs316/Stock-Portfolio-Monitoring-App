package com.prod.stockmonitor.stock_portfolio_monitor.service;

import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String register(UserClass user) {
        userRepository.save(user);
        return "User registered successfully!";
    }

    public Map<String, Object> getUserById(int id) {
        UserClass user = userRepository.findById((long) id).orElse(null);

        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("user", user);

            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.put("message", "Hello Admin!");
            } else {
                response.put("message", "Welcome, " + user.getUsername());
            }
        } else {
            response.put("error", "User not found");
        }

        return response;
    }
}
