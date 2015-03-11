package com.acc.structure;

import com.acc.constants.Kind;
import com.acc.data.Result;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by prabhuk on 2/9/2015.
 */
public class Symbol {
    private String name;
    private int suffix; //Different version of the same variable will have different suffix value.
    private SymbolType type;
    private Result value; //Could just be integer in our case.
    private int arrayDimension;
    private List<Result> arrayIdentifiers;
    private boolean isGlobal;

    /**
     * @param name           - unique symbol name in the symbol table
     * @param suffix         - -1 will represent declaration
     * @param value          - value to be assigned.
     */
    public Symbol(String name, int suffix, Result value) {
        this(name, suffix, value, SymbolType.VARIABLE);

    }

    public Symbol(String name, int suffix, Result value, SymbolType type) {
        this.name = name;
        this.suffix = suffix;
        this.type = type;
        if(value == null) {
            value = new Result(Kind.CONSTANT);
            value.value(0);
        }
        this.value = value;
    }

    public Symbol(String name, int suffix, int arrayDimension, Result value) {
        this.name = name;
        this.suffix = suffix;
        this.type = SymbolType.ARRAY;
        if(value == null) {
            value = new Result(Kind.CONSTANT);
            value.value(0);
        }
        this.value = value;
        this.arrayDimension = arrayDimension;
    }


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

    public Result getValue() {
        return value;
    }

    public void setValue(Result value) {
        this.value = value;
    }

    public SymbolType getType() {
        return type;
    }

    public String getUniqueIdentifier() {
        return type == SymbolType.VARIABLE ? (name + ":" + suffix) : name;
    }

    public List<Result> getArrayIdentifiers() {
        return arrayIdentifiers;
    }

    public void setArrayIdentifiers(List<Result> arrayIdentifiers) {
        this.arrayIdentifiers = arrayIdentifiers;
    }

    public int getArrayDimension() {
        return arrayDimension;
    }

    public void setArrayDimension(int arrayDimension) {
        this.arrayDimension = arrayDimension;
    }

    public void setSuffix(int suffix) {
        this.suffix = suffix;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }
}
