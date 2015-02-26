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
    INTERMEDIATE,
    BASE_ADDRESS,
    FRAME_POINTER;

    public boolean isConstant() {
        return this == CONSTANT;
    }

    public boolean isFramePointer() {
        return this == FRAME_POINTER;
    }

    public boolean isBaseAddress() {
        return this == BASE_ADDRESS;
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

    public boolean isIntermediate() {
        return this == INTERMEDIATE;
    }

}
