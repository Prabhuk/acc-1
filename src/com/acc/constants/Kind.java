package com.acc.constants;

/**
 * Created by Rumpy on 14-01-2015.
 */
public enum Kind {
    CONST,
    VAR,
    REG,
    CONDITION,
    FIXUP_DUMMY;

    public boolean isConst() {
        return this == CONST;
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

}
