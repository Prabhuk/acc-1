package com.acc.data;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class Instruction {

    protected final boolean isPhi;
    protected final boolean isKill;
    protected Symbol symbol;
    protected final Integer opcode;
    protected final Integer location;
    private Result x;
    private Result y;
    protected String instructionString = "";

    public Instruction(boolean isPhi, boolean isKill, Symbol symbol, Integer opcode, Integer location) {
        this.isPhi = isPhi;
        this.isKill = isKill;
        this.symbol = symbol;
        this.opcode = opcode;
        this.location = location;
    }

    public Instruction(int op, Result x, Result y, int location) {
        this.opcode = op;
        this.x = x;
        this.y = y;
        this.location = location;
        this.isPhi = OperationCode.phi == op;
        this.isKill = OperationCode.kill == op;
    }

    public Result getX() {
        return x;
    }

    public Result getY() {
        return y;
    }

    public boolean isPhi() {
        return isPhi;
    }

    public boolean isKill() {
        return isKill;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Integer getOpcode() {
        return opcode;
    }

    public Integer getLocation() {
        return location;
    }

    public String getInstructionString() {

        StringBuilder sb = new StringBuilder(OperationCode.getOperationName(opcode));
        final Integer operandCount = OperationCode.getOperandCount(opcode);
        if(operandCount > 0) {
            sb.append(" ").append(getOperand(x));
        }
        if(operandCount > 1) {
            sb.append(" ").append(getOperand(y));
        }

        return sb.toString();

    }

    private String getOperand(Result x) {
        if(x.kind().isIntermediate()) {
            return "(" + x.getIntermediateLoation() +")";
        } else if(x.kind().isConstant()) {
            return "#" + String.valueOf(x.value());
        } else if(x.kind().isVariable()) {
            if(opcode >= OperationCode.bra && opcode <= OperationCode.bgt || opcode == OperationCode.cmp) {
                return x.getVariableName();
            }
            return x.getVariableName() + ":" + symbol.getSuffix();
        } else if (x.kind().isArray()) {
            return x.getVariableName();
        }
        return "";
    }

    public void FixUp(int c) {
        if(y == null) {
            y = new Result(Kind.CONSTANT);
        }
        y.value(c);
//        this.instructionString = InstructionStringBuilder.getDLXInstruction(opcode, a, b, c, symbol, lhs, rhs);
    }
}


