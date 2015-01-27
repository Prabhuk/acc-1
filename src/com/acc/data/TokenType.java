package com.acc.data;

/**
 * Created by Rumpy on 15-01-2015.
 */
public enum TokenType {
    OPERATOR,
    KEYWORD,
    IDENTIFIER,
    CONSTANT,
    RELATIONAL_OPERATOR,
    ASSIGNMENT_OPERATOR,
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

}
