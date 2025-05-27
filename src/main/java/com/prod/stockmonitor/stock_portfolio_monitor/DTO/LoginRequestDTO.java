package com.prod.stockmonitor.stock_portfolio_monitor.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String emailId;
    private String password;
}
