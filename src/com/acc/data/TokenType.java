package com.acc.data;

/**
 * Created by Rumpy on 15-01-2015.
 */
public enum TokenType {
    /**
     * OPERATOR - are the operators allowed in an expression
     */
    OPERATOR,
    KEYWORD,
    IDENTIFIER,
    CONSTANT,
    RELATIONAL_OPERATOR,
    ASSIGNMENT_OPERATOR,
    ARRAY_IDENTIFIER,
    SEPARATOR;

    public boolean isOperator() {
        return this == OPERATOR;
    }

    public boolean isKeyword() {
        return this == KEYWORD;
    }

    public boolean isIdentifier() {
        return this == IDENTIFIER;
    }

    public boolean isConstant() {
        return this == CONSTANT;
    }

    public boolean isRelationalOperator() {
        return this == RELATIONAL_OPERATOR;
    }

    public boolean isAssignmentOperator() {
        return this == ASSIGNMENT_OPERATOR;
    }

    public boolean isSeparator() {
        return this == SEPARATOR;
    }

    public boolean isArrayIdentifier() {
        return this == ARRAY_IDENTIFIER;
    }

    public boolean isDesignator() {
        return isArrayIdentifier() || isIdentifier();
    } //$TODO$ identifier or identifier plus []

}
