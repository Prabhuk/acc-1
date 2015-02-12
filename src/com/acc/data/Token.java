package com.acc.data;

import com.acc.exception.SyntaxErrorException;

/**
 * Created by Rumpy on 15-01-2015.
 */
public abstract class Token {
    protected String token;

    public Token() {
        throw new SyntaxErrorException("Token cannot be empty");
    }

    public Token(String token) {
        this.token = token;
    }

    public abstract TokenType tokenType();

    public String getToken() {
        return token;
    }

    public boolean isSemicolon() {
        return token.equals(";");
    }

    @Override
    public String toString() {
        return token;
    }

    public boolean isOperator() {
        return this.tokenType().isOperator();
    }

    public boolean isKeyword() {
        return this.tokenType().isKeyword();
    }

    public boolean isIdentifier() {
        return this.tokenType().isIdentifier();
    }

    public boolean isConstant() {
        return this.tokenType().isConstant();
    }

    public boolean isRelationalOperator() {
        return this.tokenType().isRelationalOperator();
    }

    public boolean isAssignmentOperator() {
        return this.tokenType().isAssignmentOperator();
    }

    public boolean isSeparator() {
        return this.tokenType().isSeparator();
    }

    public boolean isArrayIdentifier() {
        return this.tokenType().isArrayIdentifier();
    }

    public boolean isDesignator() {
        return this.tokenType().isDesignator();
    }


}
