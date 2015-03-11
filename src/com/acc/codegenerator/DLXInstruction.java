package com.acc.codegenerator;

import com.acc.constants.OperationCode;
import com.acc.data.Result;

/**
 * Created by prabhuk on 3/10/2015.
 */
public class DLXInstruction {
    int opcode;
    Result a;
    Result b;
    Result c;
    Integer code;

    public DLXInstruction(int opcode, Result a, Result b, Result c) {
        this.opcode = opcode;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(OperationCode.getOperationName(opcode));
        sb.append(" ").append(getOperand(a));
        sb.append(" ").append(getOperand(b));
        sb.append(" ").append(getOperand(c));
        return sb.toString();
    }

    private String getOperand(Result x) {
        if(x == null) {
            return "";
        }
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
        } else if(x.isRegister()) {
            return "R" + x.regNo();
        }
        return "";
    }
}
