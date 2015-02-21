package com.acc.data;

import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/19/2015.
 */
public class KillInstruction extends Instruction {

    public KillInstruction(Symbol s) {
        super(true, s);
    }

    @Override
    public String getInstructionString() {
        return "KILL " + symbol.getName();
    }

    @Override
    public String getSSAString() {
        return getInstructionString();
    }
}
