package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class Instruction {
    protected final boolean isPhi = false;
    protected Symbol symbol;
    private int opcode;
    protected String instructionString;
    private Integer instruction;
    private Integer a; //if Move instruction, then this is the instruction number
    private Integer b;
    private Integer c;
    private Integer location;


    public Instruction(int instruction, int opcode, Integer a, Integer b, Integer c, Symbol symbol, Result rhs) {
        this.instruction = instruction;
        this.a = a;
        this.b = b;
        this.c = c;
        this.opcode = opcode;
        this.symbol = symbol;
        instructionString = getInstructionAsString(symbol, opcode, a, b, c, rhs);
    }

    //Made for Phi Instruction
    protected Instruction(int opcode) {
        this.opcode = opcode;
    }

    public boolean isPhi() {
        return isPhi;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setC(Integer _c) {
        c = _c;
        instructionString = getInstructionAsString(symbol, opcode, a, b, c, null);
    }

    public void setInstruction(Integer instruction) {
        this.instruction = instruction;
    }

    protected String getInstructionAsString(Symbol symbol, int opcode, Integer a, Integer b, Integer c, Result rhs) {
        final String operationName = OperationCode.opcodeAndNames.get(opcode);
        final StringBuilder sb = new StringBuilder(operationName).append(" ");
        boolean addComma = false;
        if (a != null) {
            if (opcode == 15) {
                sb.append(symbol.getUniqueIdentifier());
            } else {
                sb.append(String.valueOf(a));
            }
            addComma = true;
        }
        if (opcode == 15) { //mov
            buildMoveInstruction(b, c, sb, addComma, rhs);
        } else {
            if (b != null) {
                if (addComma) {
                    sb.append(",");
                }
                addComma = true;
                sb.append(String.valueOf(b));
            }
            if (c != null) {
                if (addComma) {
                    sb.append(",");
                }
                sb.append(String.valueOf(c));
            }
        }
        return sb.toString();
    }

    private void buildMoveInstruction(Integer b, Integer c, StringBuilder sb, boolean addComma, Result rhs) {
        if (b == null) {
            return;
        }
        if (addComma) {
            sb.append(",");
        }
        if (c == 1) { //variable
            sb.append("(").append(rhs.getVariableName()).append(")");
        } else {
            sb.append(String.valueOf(b));
        }
    }


    /*
     * Returns the integer form of the instruction
     */
    public Integer getInstruction() {
        return instruction;
    }


    public void FixUp(int c) {
        instruction = instruction & 0xffff0000 + c;
        this.c = c;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return instructionString;
    }

    public String getNewIdentifierForSymbol() {
        return symbol.getName() + ":" + location;
    }

    public String getInstructionString() {
        return instructionString;
    }
}
