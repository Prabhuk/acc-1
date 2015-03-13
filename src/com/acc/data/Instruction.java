package com.acc.data;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;
import com.acc.vm.Register;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class Instruction {

    protected Symbol symbol;
    protected final Integer opcode;
    protected Integer location;
    protected Result x;
    protected Result y;
    protected final Set<Integer> liveRanges = new HashSet<Integer>();
    protected List<Result> parameters;

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
        if(opcode == OperationCode.noop) {
            return "";
        }

        StringBuilder sb = new StringBuilder(OperationCode.getOperationName(opcode));
        final Integer operandCount = OperationCode.getOperandCount(opcode);

        if(opcode == OperationCode.phi) {
            sb.append(" ").append(symbol.getName());
        }

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

        if(isCall() && parameters != null) {
            for (Result parameter : parameters) {
                sb.append(" ").append(getOperand(parameter));
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
            if(OperationCode.move == opcode) {
                symbol.setSuffix(location);
                x.setLocation(location);
            }
            return x.getVariableName() + ":" + x.getLocation();
        } else if (x.isArray()) {
            return x.getVariableName();
        } else if (x.isFramePointer()) {
            return "FP:0";
        } else if (x.isBaseAddress()) {
            return x.getVariableName() + ":baseaddress";
        } else if(x.isRegister()) {
            if(Register.ssaToDLX.get(x.regNo()) != null) {
                return "R[" + Register.ssaToDLX.get(x.regNo()) + "]";
            } else {
                return "R[" + x.regNo() + "]";
            }
        }
        return "";
    }

    public void fixup(int c) {
        if(opcode == OperationCode.bra) {
            changeResultsTargetValue(x, c);
        } else {
            changeResultsTargetValue(y, c);
        }
    }

    private void changeResultsTargetValue(Result x, int c) {
        if(x == null) {
            setX(new Result(Kind.CONSTANT));
        }
        x.value(c);
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

    public Set<Integer> getLiveRanges() {
        return liveRanges;
    }

    public void addToLiveRanges(Set<Integer> liveRanges) {
        this.liveRanges.addAll(liveRanges);
    }

    public List<Result> getParameters() {
        return parameters;
    }

    public void setParameters(List<Result> parameters) {
        this.parameters = parameters;
    }

    public boolean isCall() {
        return opcode == OperationCode.call;
    }
}


