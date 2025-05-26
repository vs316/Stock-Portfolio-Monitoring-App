package com.prod.stockmonitor.stock_portfolio_monitor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String emailid;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "fullName is required")
    private String fullName;


    @Pattern(regexp = "^(admin|user)$", message = "Role must be 'admin' or 'user'")
    private String role;

}