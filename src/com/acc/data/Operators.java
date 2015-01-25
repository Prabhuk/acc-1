package com.acc.data;

/**
 * Created by prabhuk on 1/24/2015.
 */
public enum Operators {
    PLUS("+"),
    MINUS("-"),
    DIVISION("/"),
    PRODUCT("*");

    private final String symbol;

    Operators(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isPlus() {
        return PLUS.getSymbol().equals(getSymbol());
    }

    public boolean isMinus() {
        return MINUS.getSymbol().equals(getSymbol());
    }

    public boolean isDivision() {
        return DIVISION.getSymbol().equals(getSymbol());
    }

    public boolean isMultiplication() {
        return PRODUCT.getSymbol().equals(getSymbol());
    }
}
