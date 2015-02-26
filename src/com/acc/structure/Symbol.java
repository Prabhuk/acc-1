package com.acc.structure;

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
    private Integer value; //Could just be integer in our case.
    private Object arrayValue;
    private int arrayDimension;
    private List<Result> arrayIdentifiers;

    /**
     * @param name           - unique symbol name in the symbol table
     * @param suffix         - -1 will represent declaration
     * @param value          - value to be assigned.
     */
    public Symbol(String name, int suffix, Integer value) {
        this(name, suffix, value, SymbolType.VARIABLE);

    }

    public Symbol(String name, int suffix, Integer value, SymbolType type) {
        this.name = name;
        this.suffix = suffix;
        this.type = type;
        this.value = value;
    }

    public Symbol(String name, int suffix, int arrayDimension, Object value) {
        this.name = name;
        this.suffix = suffix;
        this.type = SymbolType.ARRAY;
        this.arrayValue = value;
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setArrayValue(Object value) {
        this.arrayValue = value;
    }

    //based on http://stackoverflow.com/questions/869033/how-do-i-copy-an-object-in-java
    public static Object cloneValue(Object obj) {
        try {
            if (obj.getClass() == Integer.class) {
                return new Integer((Integer) obj);
            }
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

    public Object getArrayValue() {
        return arrayValue;
    }
}
