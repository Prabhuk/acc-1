package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/14/2015.
 */
public class PhiInstruction extends Instruction {
    private Symbol leftSymbol;
    private Symbol rightSymbol;

    public PhiInstruction(Symbol symbol) {
        super(OperationCode.PHI, true);
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

    public boolean canIgnore() {
        return leftSymbol == rightSymbol || leftSymbol == null || rightSymbol == null;
    }

    public String getInstructionString() {
//        if(canIgnore()) {
//            return "";
//        }
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

    public String getSSAString() {
//        if(canIgnore()) {
//            return "";
//        }
        StringBuilder sb = new StringBuilder("phi ");
        if(leftSymbol != null) {
            sb.append(leftSymbol.getName()).append(" ");
        }
        if(rightSymbol != null) {
            sb.append(rightSymbol.getName());
        }
        return sb.toString();
    }
}
