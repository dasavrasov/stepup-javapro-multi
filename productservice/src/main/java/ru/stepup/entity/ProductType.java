package ru.stepup.entity;

public enum ProductType {
    CARD(0,"Карта"),
    ACCOUNT(1,"Счет");

    private final int code;
    private final String description;

    ProductType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductType getByCode(int code) {
        for (ProductType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Неверное значение поля product_type " + code);
    }

}
