package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserClass u) {
        String response = String.valueOf(userService.registerUser(u));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/getUserById", produces = "application/json")
    public ResponseEntity<?> getUserById(@RequestParam Long userId) {
        return userService.getUserById(userId);
    }
//
//        @PostMapping("/checkUser")
//        public ResponseEntity<?> checkUser(@RequestParam String emailId) {
//            return userService.checkUserByEmail(emailId);
//        }


}
