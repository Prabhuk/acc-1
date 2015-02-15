package com.acc.structure;

import java.lang.reflect.Field;

/**
 * Created by prabhuk on 2/9/2015.
 */
public class Symbol {
    private String name;
    private int suffix; //Different version of the same variable will have different suffix value.
    private int length; //For arrays
    private SymbolType type;
    private boolean isPointerValue;
    private Object value; //Could just be integer in our case.
    private int arrayDimension; //Could just be integer in our case.

    /**
     * @param name           - unique symbol name in the symbol table
     * @param suffix         - -1 will represent declaration
     * @param type           - variable or procedure
     * @param isPointerValue - true if assigning result of another instruction, false if constant
     * @param value          - value to be assigned. $TODO$ make it integer?
     */
    public Symbol(String name, int suffix, SymbolType type, boolean isPointerValue, Object value) {
        this.name = name;
        this.suffix = suffix;
        this.type = type;
        this.isPointerValue = isPointerValue;
        this.value = value;
    }

    public Symbol(Symbol symbol) {
        name = symbol.getName();
        suffix = -1; //should be set by the caller in due course
        type = symbol.getType();
        isPointerValue = symbol.isPointerValue();
        value = Symbol.cloneValue(symbol.getValue());
    }

    public void setArrayDimension(int arrayDimension) {
        this.arrayDimension = arrayDimension;
    }

    public int getArrayDimension() {
        return arrayDimension;
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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    //based on http://stackoverflow.com/questions/869033/how-do-i-copy-an-object-in-java
    public static Object cloneValue(Object obj) {
        try {
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        } catch (Exception e) {
            return null;
        }
    }

    public int getLength() {
        return length;
    }

    public SymbolType getType() {
        return type;
    }

    public boolean isPointerValue() {
        return isPointerValue;
    }

    public String getUniqueIdentifier() {
        return name + ":" + suffix;
    }
}
