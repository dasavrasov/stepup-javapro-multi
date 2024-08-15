package ru.stepup.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentRequest {
    private User user;
    private String account;
    private BigDecimal amount;
}
