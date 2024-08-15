package ru.stepup.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductResponse {
	private int id;
	private String account;
	private BigDecimal balance;
	private String productType;
	private User userId;
}
