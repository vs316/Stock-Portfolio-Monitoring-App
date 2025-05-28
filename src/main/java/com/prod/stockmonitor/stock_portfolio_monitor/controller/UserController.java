package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import com.prod.stockmonitor.stock_portfolio_monitor.DTO.LoginRequestDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.LoginResponseDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.DTO.UserUpdateDTO;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.model.UserRepository;
import com.prod.stockmonitor.stock_portfolio_monitor.security.JwtUtil;
import com.prod.stockmonitor.stock_portfolio_monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserClass u) {
        String response = String.valueOf(userService.registerUser(u));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/getUserById", produces = "application/json")
    public ResponseEntity<?> getUserById(@RequestParam Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO updateDTO) {
        return userService.updateUserById(id, updateDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        UserClass user = userRepository.findByEmailIdIgnoreCase(loginRequest.getEmailId());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmailId());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password, Please go to the register");
        }
    }


}