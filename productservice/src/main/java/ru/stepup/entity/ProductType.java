package ru.stepup.entity;

public enum ProductType {
    CARD("0","Карта"),
    ACCOUNT("1","Счет");

    private final String code;
    private final String description;

    ProductType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductType getByCode(String code) {
        for (ProductType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неверное значение поля product_type " + code);
    }

}
