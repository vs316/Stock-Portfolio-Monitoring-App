package com.prod.stockmonitor.stock_portfolio_monitor.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserClass, Long> {
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailidIgnoreCase(String emailid);
}
