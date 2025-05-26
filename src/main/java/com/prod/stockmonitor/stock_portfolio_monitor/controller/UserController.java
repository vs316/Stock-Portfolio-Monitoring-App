package com.prod.stockmonitor.stock_portfolio_monitor.controller;

import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import com.prod.stockmonitor.stock_portfolio_monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserClass u) {
        String response = userService.register(u);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getUser")
    public ResponseEntity<?> getUserById(@RequestParam int id) {
        UserClass user = (UserClass) userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}
