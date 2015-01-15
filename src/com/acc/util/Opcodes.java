package com.acc.util;

/**
 * Created by prabhuk on 1/14/2015.
 */
public enum Opcodes {
    ADD(0)
    ;
    int opcode;

    Opcodes(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }

}
