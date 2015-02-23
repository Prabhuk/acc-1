package com.acc.data;

import com.acc.structure.Symbol;
import com.acc.util.InstructionStringBuilder;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class Instruction {

    protected final boolean isPhi;
    protected final boolean isKill;
    protected final Symbol symbol;
    protected final Symbol rhs; //MOV specific
    protected final Symbol lhs; //MOV specific
    protected final int opcode;
    protected Integer instruction;
    protected final Integer a; //if Move instruction, then this is the instruction number
    protected final Integer b;
    protected Integer c;
    protected final Integer location;

    protected String instructionString;
    private String ssaIntruction;

    public Instruction(Integer instruction, int opcode, Integer a, Integer b, Integer c, Symbol symbol,
                       boolean isPhi, boolean isKill, Symbol lhs, Symbol rhs, int location) {
        this.instruction = instruction;
        this.a = a;
        this.b = b;
        this.c = c;
        this.opcode = opcode;
        this.symbol = symbol;
        this.isPhi = isPhi;
        this.isKill = isKill;
        this.lhs = lhs;
        this.rhs = rhs;
        this.location = location;
        this.ssaIntruction = InstructionStringBuilder.getSSA(opcode, a, b, c, symbol, lhs, rhs, this);
        this.instructionString = InstructionStringBuilder.getDLXInstruction(opcode, a, b, c, symbol, lhs, rhs);
    }

    public boolean isKill() {
        return isKill;
    }

    public boolean isPhi() {
        return isPhi;
    }

    public int getOpcode() {
        return opcode;
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
        this.ssaIntruction = InstructionStringBuilder.getSSA(opcode, a, b, c, symbol, lhs, rhs, this);
        this.instructionString = InstructionStringBuilder.getDLXInstruction(opcode, a, b, c, symbol, lhs, rhs);
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Integer getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return getInstructionString();
    }

    public String getNewIdentifierForSymbol() {
        return symbol.getName() + ":" + location;
    }

    public String getSSAString() {
        return ssaIntruction;
    }



    public String getInstructionString() {
        return instructionString;
    }
}
