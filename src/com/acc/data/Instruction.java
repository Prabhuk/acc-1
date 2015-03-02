package com.acc.data;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class Instruction {

    protected Symbol symbol;
    protected final Integer opcode;
    protected Integer location;
    protected Result x;
    protected Result y;

    private String deletePurpose;
    private boolean isDeleted;

    public Instruction(int op, Result x, Result y, int location) {
        this.opcode = op;
        this.x = x;
        this.y = y;
        this.location = location;
    }

    public Result getX() {
        return x;
    }

    public Result getY() {
        return y;
    }

    public boolean isPhi() {
        return OperationCode.phi == opcode;
    }

    public boolean isKill() {
        return OperationCode.kill == opcode;
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
            if(opcode == OperationCode.move || opcode == OperationCode.store) {
                sb.append(" ").append(getOperand(y));
            } else {
                sb.append(" ").append(getOperand(x));
            }
        }
        if(operandCount > 1) {
            if(opcode == OperationCode.move || opcode == OperationCode.store) {
                sb.append(" ").append(getOperand(x));
            } else {
                sb.append(" ").append(getOperand(y));
            }
        }

        return sb.toString();

    }

    private String getOperand(Result x) {
        if(x.isIntermediate()) {
            return "(" + x.getIntermediateLoation() +")";
        } else if(x.isConstant()) {
            return "#" + String.valueOf(x.value());
        } else if(x.isVariable() || x.isProcedure()) {
            if(opcode == OperationCode.cmp || opcode == OperationCode.kill || x.getLocation() == null) {
                return x.getVariableName();
            }
            return x.getVariableName() + ":" + x.getLocation();
        } else if (x.isArray()) {
            return x.getVariableName();
        } else if (x.isFramePointer()) {
            return "FP:0";
        } else if (x.isBaseAddress()) {
            return x.getVariableName() + ":baseaddress";
        }
        return "";
    }

    public void FixUp(int c) {
        if(y == null) {
            setY(new Result(Kind.CONSTANT));
        }
        y.value(c);
    }

    public void setX(Result x) {
        this.x = x;
    }

    public void setY(Result y) {
        this.y = y;
    }

    public String getDeletePurpose() {
        return deletePurpose;
    }

    public void setDeletePurpose(String deletePurpose) {
        this.deletePurpose = deletePurpose;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted, String deletePurpose) {
        this.isDeleted = isDeleted;
        this.deletePurpose = deletePurpose;
    }


    public boolean isComplete() {
        if(opcode != OperationCode.phi) {
            throw new UnsupportedOperationException("This operation is only for PHI instructions");
        }
        return y != null && x != null;
    }

    public boolean canIgnore() {
        if(opcode != OperationCode.phi) {
            throw new UnsupportedOperationException("This operation is only for PHI instructions");
        }
        return Result.isSameVariable(x, y) || !isComplete();
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return getInstructionString();
    }
}


