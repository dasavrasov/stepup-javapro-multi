package ru.stepup.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Limit {
    private Long id;

    private Long userId;

    private BigDecimal value;

    private LocalDateTime createdAt;

    public BigDecimal getValue() {
        return value;
    }
}