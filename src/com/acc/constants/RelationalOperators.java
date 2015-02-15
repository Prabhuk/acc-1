package com.acc.constants;

/**
 * Created by Rumpy on 12-02-2015.
 */
public enum RelationalOperators {
    GREATER_THAN(">"),
    GREATER_THAN_EQUALS_TO(">="),
    LESSER_THAN("<"),
    LESSER_THAN_EQUALS_TO("<="), EQUALS_TO("=="), NOT_EQUALS_TO("!=");

    private final String symbol;

    RelationalOperators(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isGreaterThan() {
        return GREATER_THAN.getSymbol().equals(getSymbol());
    }

    public boolean isLesserThan() {
        return LESSER_THAN.getSymbol().equals(getSymbol());
    }

    public boolean isGreaterThanEqualsTo() {
        return GREATER_THAN_EQUALS_TO.getSymbol().equals(getSymbol());
    }

    public boolean isLesserThanEqualsTo() {
        return LESSER_THAN_EQUALS_TO.getSymbol().equals(getSymbol());
    }

    public boolean isEqualsTo() {
        return EQUALS_TO.getSymbol().equals(getSymbol());
    }

    public boolean isNotEqualsTo() {
        return NOT_EQUALS_TO.getSymbol().equals(getSymbol());
    }
}
