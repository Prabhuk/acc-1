package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/19/2015.
 */
public class KillInstruction extends Instruction {

    public KillInstruction(Symbol s, int pc) {
        super(null, OperationCode.KILL, null, null, null, s, false, true, null, null, pc);
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
