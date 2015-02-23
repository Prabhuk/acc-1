package com.acc.data;

/**
 * Created by prabhuk on 2/22/2015.
 */
public class SSAInstruction {
    private final int location;
    String name;
    String operand1;
    String operand2;

    public SSAInstruction(String name, String operand1, String operand2, int location) {
        this.name = name;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.location = location;
    }

    public String getInstruction() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if(operand1 != null) {
            sb.append(" ").append(operand1);
        }

        if(operand2 != null) {
            sb.append(" ").append(operand2);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getInstruction();
    }

    public String getName() {
        return name;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public int getLocation() {
        return location;
    }
}
