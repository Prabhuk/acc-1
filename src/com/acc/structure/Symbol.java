package com.acc.structure;

/**
 * Created by prabhuk on 2/9/2015.
 */
public class Symbol {
    private String name;
    private int suffix; //Different version of the same variable will have different suffix value.
    private int length; //For arrays
    private SymbolType type;
    private Object value; //Could just be integer in our case.

    private boolean isProcedure() {
        return this.type.isProcedure();
    }

    private boolean isVariable() {
        return this.type.isVariable();
    }

    public int getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("You cannot change the name of the variable");
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
