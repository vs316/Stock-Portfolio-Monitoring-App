package com.prod.stockmonitor.stock_portfolio_monitor.service;


import com.prod.stockmonitor.stock_portfolio_monitor.DTO.UserResponseDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.UserUpdateDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<UserResponseDTO> getUserById(Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body(new UserResponseDTO("User ID cannot be null"));
        }

        UserClass user = userRepository.findById(userId).orElse(null);

        if (user == null || user.getEmailId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserResponseDTO("User not found or emailId is missing"));
        }

        UserResponseDTO response = new UserResponseDTO();
        response.setUsername(user.getUsername());
        response.setEmailId(user.getEmailId());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.setMessage("Hey Admin!");
        }

        return ResponseEntity.ok(response);
    }

    @Transactional
    public UserClass registerUser(UserClass user) {
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public ResponseEntity<?> updateUserById(Long id, UserUpdateDTO dto) {
        UserClass user = userRepository.findById(id).orElse(null);
        if (user == null) {
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
