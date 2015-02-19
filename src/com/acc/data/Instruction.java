package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class Instruction {
    protected boolean isPhi = false;
    private Symbol rhs; //MOV specific
    private Symbol lhs; //MOV specific
    protected Symbol symbol;
    private int opcode;
    protected String instructionString;
    private Integer instruction;
    private Integer a; //if Move instruction, then this is the instruction number
    private Integer b;
    private Integer c;
    private Integer location;
    
    private static List<Integer> excludeB = new ArrayList<Integer>();
    private static List<Integer> excludeA = new ArrayList<Integer>();

    static {
        excludeA.add(OperationCode.BSR);
        excludeA.add(OperationCode.JSR);
        excludeA.add(OperationCode.RET);

        excludeB.add(OperationCode.CHK);
        excludeB.add(OperationCode.CHKI);
        excludeB.add(OperationCode.BEQ);
        excludeB.add(OperationCode.BNE);
        excludeB.add(OperationCode.BLT);
        excludeB.add(OperationCode.BGE);
        excludeB.add(OperationCode.BLE);
        excludeB.add(OperationCode.BGT);
        excludeB.add(OperationCode.BSR);
        excludeB.add(OperationCode.JSR);
        excludeB.add(OperationCode.RET);



//    OperationCode.RDD
//    OperationCode.WRD
//    OperationCode.WRH
//    OperationCode.WRL
    }


    public Instruction(int instruction, int opcode, Integer a, Integer b, Integer c, Symbol symbol) {
        this.instruction = instruction;
        this.a = a;
        this.b = b;
        this.c = c;
        this.opcode = opcode;
        this.symbol = symbol;
   }

    //Made for mov instruction
    public Instruction(Symbol rhs, Symbol lhs) {
        this.opcode = OperationCode.MOV;
        this.rhs = rhs;
        this.lhs = lhs;
        this.symbol = lhs;
    }

    //Made for Phi Instruction
    protected Instruction(int opcode, boolean isPhi) {
        this.opcode = opcode;
        this.isPhi = isPhi;
    }

    public boolean isPhi() {
        return isPhi;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setC(Integer _c) {
        c = _c;
    }

    public void setInstruction(Integer instruction) {
        this.instruction = instruction;
    }

    private void buildMoveInstruction(StringBuilder sb) {
        if(rhs.getResult().kind().isRegister()) {
            sb.append("(R").append(rhs.getResult().regNo()).append(")");
        } else if (rhs.getResult().kind().isVariable()) { //variable
            //Should be address field
            sb.append("(").append(String.valueOf(rhs.getResult().address())).append(")");
        } else {
            sb.append(String.valueOf(rhs.getResult().value()));
        }

        if(lhs.getType().isArray()) {
            sb.append(",").append(lhs.getUniqueIdentifier());
            for (Result result : lhs.getArrayIdentifiers()) {
                //$TODO$ result types must be handled
                sb.append("[");
                if(result.kind().isConstant()) {
                    sb.append(result.value());
                } else if (result.kind().isVariable()) {
                    sb.append("(");
                    sb.append(result.address());
                    sb.append(")");
                } else {
                    sb.append("(R");
                    sb.append(result.regNo());
                    sb.append(")");
                }
                sb.append("]");
            }
        } else {
            sb.append(",").append(lhs.getUniqueIdentifier());
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
        return getInstructionString();
    }

    public String getNewIdentifierForSymbol() {
        return symbol.getName() + ":" + location;
    }

    public String getInstructionString() {
        final String operationName = OperationCode.opcodeAndNames.get(opcode);
        final StringBuilder sb = new StringBuilder(operationName).append(" ");
        if (opcode == OperationCode.MOV) {
            buildMoveInstruction(sb);
        } else {
            boolean addComma = false;
            if (a != null && !excludeA.contains(opcode)) {
                if (opcode == 15) {
                    sb.append(symbol.getUniqueIdentifier());
                } else {
                    sb.append(String.valueOf(a));
                }
                addComma = true;
            }
            if (b != null && !excludeB.contains(opcode)) {
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

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
