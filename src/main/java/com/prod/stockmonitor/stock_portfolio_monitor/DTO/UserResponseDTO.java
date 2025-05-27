package com.prod.stockmonitor.stock_portfolio_monitor.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Ensures null fields are excluded
@Getter
@Setter
public class UserResponseDTO {
    private String username;
    private String emailId;
    private String fullName;
    private String role;
    private String message;

    // Constructors
    public UserResponseDTO() {}

    public UserResponseDTO(String message) {
        this.message = message;
    }
}