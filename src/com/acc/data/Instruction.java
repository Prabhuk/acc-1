package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class Instruction {
    private Symbol symbol;
    private int opcode;
    private String instructionString;
    private Integer instruction;
    private Integer a; //if Move instruction, then this is the instruction number
    private Integer b;
    private Integer c;


    public Instruction(Integer ins) {
        this.instruction = ins;
    }

    public Instruction(int _instruction, int _opcode, Integer a, Integer b, Integer c, Symbol symbol) {
        this.instruction = _instruction;
        this.a = a;
        this.b = b;
        this.c = c;
        this.opcode = _opcode;
        this.symbol = symbol;
        instructionString = getInstructionAsString(symbol, opcode, a, b, c);
    }

    public void setC(Integer _c) {
        c = _c;
        instructionString = getInstructionAsString(symbol, opcode, a, b, c);
    }

    public void setInstruction(Integer instruction) {
        this.instruction = instruction;
    }

    private String getInstructionAsString(Symbol symbol, int opcode, Integer a, Integer b, Integer c) {
        final String operationName = OperationCode.opcodeAndNames.get(opcode);
        final StringBuilder sb = new StringBuilder(operationName).append(" ");
        boolean addComma = false;
        if(a != null) {
            if(opcode == 15) {
                sb.append(symbol.getUniqueIdentifier());
            } else {
                sb.append(String.valueOf(a));
            }
            addComma = true;
        }
        if(opcode == 15) { //mov
            buildMoveInstruction(b, c, sb, addComma);
        } else {
            if(b != null) {
                if(addComma) {
                    sb.append(",");
                }
                addComma = true;
                sb.append(String.valueOf(b));
            }
            if(c != null) {
                if(addComma) {
                    sb.append(String.valueOf(c));
                }
            }
        }
        return sb.toString();
    }

    private void buildMoveInstruction(Integer b, Integer c, StringBuilder sb, boolean addComma) {
        if(b==null) {
            return;
        }
        if(addComma) {
            sb.append(",");
        }
        if(c==1) { //constant
            sb.append("(").append(String.valueOf(b)).append(")");
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

    @Override
    public String toString() {
        return instructionString;
    }
}
