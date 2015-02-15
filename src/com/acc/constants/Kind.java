package com.acc.constants;

/**
 * Created by Rumpy on 14-01-2015.
 */
public enum Kind {
    CONSTANT,
    VAR,
    ARRAY,
    REG,
    CONDITION,
    FIXUP_DUMMY;

    public boolean isConstant() {
        return this == CONSTANT;
    }

    public boolean isVariable() {
        return this == VAR;
    }

    public boolean isRegister() {
        return this == REG;
    }

    public boolean isCondition() {
        return this == CONDITION;
    }

    public boolean isArray() {
        return this == ARRAY;
    }

}
