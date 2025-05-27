package com.prod.stockmonitor.stock_portfolio_monitor.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    @NotBlank(message = "Full Name is required")
    private String fullName;

    @NotBlank(message="Username is required")
    private String username;

    @Email(message="Invalid email format")
    @NotBlank(message = "Email is required")
    private String emailId;
    @NotBlank(message = "Password is required")
    private String password;
}
