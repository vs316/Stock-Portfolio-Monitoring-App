package com.prod.stockmonitor.stock_portfolio_monitor.repository;

import com.prod.stockmonitor.stock_portfolio_monitor.model.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserClass, Long> {
    UserClass findByEmailIdIgnoreCase(String emailId);


    Optional<UserClass> findById( Long userId);
}
