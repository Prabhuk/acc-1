package com.acc.structure;

/**
 * Created by prabhuk on 2/11/2015.
 */
public enum SymbolType {
    PROCEDURE,
    VARIABLE,
    ARRAY;

    public boolean isProcedure() {
        return this == PROCEDURE;
    }

    public boolean isVariable() {
        return this == VARIABLE;
    }

    public boolean isArray() {
        return this == ARRAY;
    }
}
