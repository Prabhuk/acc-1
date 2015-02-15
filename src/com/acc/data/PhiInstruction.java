package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/14/2015.
 */
public class PhiInstruction extends Instruction {
    protected final boolean isPhi = true;
    private Symbol leftSymbol;
    private Symbol rightSymbol;

    public PhiInstruction(Symbol symbol) {
        super(OperationCode.PHI);
        this.symbol = symbol;

    }

    public Symbol getLeftSymbol() {
        return leftSymbol;
    }

    public void setLeftSymbol(Symbol leftSymbol) {
        this.leftSymbol = leftSymbol;
    }

    public Symbol getRightSymbol() {
        return rightSymbol;
    }

    public void setRightSymbol(Symbol rightSymbol) {
        this.rightSymbol = rightSymbol;
    }

    public boolean isComplete() {
        return rightSymbol != null && leftSymbol != null;
    }

    public String getInstructionString() {
        StringBuilder sb = new StringBuilder("phi ");
        if(leftSymbol != null) {
            sb.append(leftSymbol.getUniqueIdentifier()).append(" ");
        }
        if(rightSymbol != null) {
            sb.append(rightSymbol.getUniqueIdentifier());
        }
        instructionString = sb.toString();
        return instructionString;
    }
}
