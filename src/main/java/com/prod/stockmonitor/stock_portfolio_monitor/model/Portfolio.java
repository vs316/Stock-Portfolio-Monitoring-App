package com.prod.stockmonitor.stock_portfolio_monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List; // Import List

@Entity
@Table(name = "portfolio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserClass user;

    @Column(nullable = false)
    private String portfolioName;


    // cascade = CascadeType.ALL ensures that if a Portfolio is deleted, its StockHoldings are also deleted.
    // orphanRemoval = true ensures that if a StockHolding is removed from the list, it's deleted from the DB.
    // mappedBy = "portfolio" indicates the owning side of the relationship (StockHolding has a 'portfolio' field).
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StockHolding> stockHoldings;
}