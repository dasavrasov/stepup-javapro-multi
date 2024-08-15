package ru.stepup.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    private Long id;
    private String account;
    private BigDecimal balance;
    private User userId;
}
