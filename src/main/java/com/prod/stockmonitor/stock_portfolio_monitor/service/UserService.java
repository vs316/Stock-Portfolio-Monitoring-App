package com.prod.stockmonitor.stock_portfolio_monitor.service;

//import com.prod.stockmonitor.stock_portfolio_monitor.model.Portfolio;
//import com.prod.stockmonitor.stock_portfolio_monitor.model.PortfolioRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.UserResponseDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.UserUpdateDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PortfolioRepository portfolioRepository;  // Add Portfolio support
//
//    public ResponseEntity<?> checkUserByEmail(String emailId) {
//        UserClass user = userRepository.findByEmailIdIgnoreCase(emailId);
//        if (user != null) {
//            if ("admin".equalsIgnoreCase(user.getRole())) {
//                // Return all portfolios
//                List<Portfolio> allPortfolios = portfolioRepository.findAll();
//                return ResponseEntity.ok(Map.of("user", hideSensitive(user), "portfolios", allPortfolios));
//            } else {
//                // Return only this user's portfolio
//                List<Portfolio> userPortfolios = portfolioRepository.findByUserId(user.getId());
//                return ResponseEntity.ok(Map.of("user", hideSensitive(user), "portfolios", userPortfolios));
//            }
//        } else {
//            return ResponseEntity.ok(Map.of("message", "New user. Please provide full name, username, and password."));
//        }
//    }

    public ResponseEntity<UserResponseDTO> getUserById(Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new UserResponseDTO("User ID cannot be null"));
        }

        UserClass user = userRepository.findById(id).orElse(null);

        if (user == null || user.getEmailId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserResponseDTO("User not found or emailId is missing"));
        }

        UserResponseDTO response = new UserResponseDTO();
        response.setUsername(user.getUsername());
        response.setEmailId(user.getEmailId());  // Will only be set if `emailId` is non-null
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.setMessage("Hey Admin!");
        }

        return ResponseEntity.ok(response);
    }

//    private UserClass hideSensitive(UserClass user) {
//        user.setPassword(null); // Hide password
//        return user;
//    }

//    public String register(UserClass user) {
//        // Hash password before saving
//        user.setPassword(passwordEncoder().encode(user.getPassword()));
//        userRepository.save(user);
//        return "User registered successfully!";
//    }
    @Transactional
    public UserClass registerUser(UserClass user) {
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }
    @Transactional
    public ResponseEntity<?> updateUserById(Long id, UserUpdateDTO dto){
        UserClass user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");

        }
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmailId(dto.getEmailId());
        user.setPassword(passwordEncoder().encode(dto.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Details are updated successfully");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
