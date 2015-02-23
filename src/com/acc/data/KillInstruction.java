package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/19/2015.
 */
public class KillInstruction extends Instruction {

    public KillInstruction(Symbol s, int pc) {
        super(false, true, s, OperationCode.kill, pc);
    }
}
