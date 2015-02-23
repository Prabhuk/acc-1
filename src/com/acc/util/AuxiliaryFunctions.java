package com.acc.util;

import com.acc.constants.Condition;
import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.memory.RegisterAllocator;
import com.acc.structure.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * The auxilary methods to cre
 */
public class AuxiliaryFunctions {


    public static void putF1(Code code, int instructionCode, int a, int b, int c, Symbol symbol) {
        if (c < 0) c ^= 0xFFFF0000;
        final int ins = instructionCode << 26 | a << 21 | b << 16 | c;
        final Instruction instruction = new Instruction(ins, instructionCode, a, b, c, symbol, false, false, null, null, code.getPc());
        code.addCode(instruction);
    }

    public static void putMOV(Code code, Symbol rhs, Symbol lhs) {
        code.addCode(new Instruction(null, OperationCode.MOV, 0, 0, 0, lhs, false, false, lhs, rhs, code.getPc()));
        putF1(code, OperationCode.STW, 0, 0, 0, lhs);
    }

    public static void putF2(Code code, int instructionCode, int a, int b, int c, Symbol symbol) {
        final int ins = instructionCode << 26 | a << 21 | b << 16 | c;
        final Instruction instruction = new Instruction(ins, instructionCode, a, b, c, symbol, false, false, null, null, code.getPc());
        code.addCode(instruction);
    }

    public static void putF3(Code code, int instructionCode, int c) {
        final int ins = instructionCode << 26 | c;
        final Instruction instruction = new Instruction(ins, instructionCode, null, null, c, null, false, false, null, null, code.getPc());
        code.addCode(instruction);
    }

    public static void BJ(Code code, int loc, BasicBlock loopBlock) {
        final Symbol symbol = new Symbol(String.valueOf(loc), code.getPc(), null, false, loc);
        symbol.setResult(new Result(Kind.CONSTANT, null, 0, null, null, null));
        putF1(code, OperationCode.BEQ, 0, 0, loc, symbol);
    }

    public static void FJLink(Code code, Result x) {
        final Symbol symbol = new Symbol(String.valueOf(x.fixupLoc()), code.getPc(), null, false, x.fixupLoc());
        symbol.setResult(x);
        putF1(code, OperationCode.BEQ, 0, 0, x.fixupLoc(), symbol);
        x.fixupLoc(code.getPc() - 1);
    }

    public static void CJF(Code code, Result x) {
        final Symbol symbol = new Symbol(String.valueOf(x.regNo()), code.getPc(), null, false, x.regNo());
        symbol.setResult(x);
        putF1(code, Condition.getNegatedInstruction(x.condition()), x.regNo(), 0, 0, symbol);
        RegisterAllocator.deallocate(x.regNo());
        x.fixupLoc(code.getPc() - 1);
    }

    public static void generateSSA(SSACode ssaCode, String ssaInstruction, Result x, Result y) {
        String op1 = null;
        if(x.kind().isVariable()) {
            op1 = x.getVariableName();
        } else if(x.kind().isConstant()) {
            op1 = String.valueOf(x.value());
        }
        String op2 = null;
        if(y.kind().isVariable()) {
            op2 = y.getVariableName();
        } else if(y.kind().isConstant()) {
            op2 = String.valueOf(y.value());
        }
        ssaCode.addCode(new SSAInstruction(ssaInstruction, op1, op2, ssaCode.getPc()));
    }

    /*
     * Combines x & y and resultant result obj is maintained in x.
     */
    public static void combine(Code code, int op, Result x, Result y) {
        if (x.kind().isConstant() && y.kind().isConstant()) {
            if (op == OperationCode.ADD) {
                x.value(x.value() + y.value());
            } else if (op == OperationCode.SUB) {
                x.value(x.value() - y.value());
            } else if (op == OperationCode.MUL) {
                x.value(x.value() * y.value());
            } else if (op == OperationCode.DIV) {
                x.value(x.value() / y.value());
            } else {
                throw new UnsupportedOperationException("Combine cannot process Operation code [" + op + "]");
            }
            x.kind(Kind.CONSTANT);
        } else {
            load(code, x);
            if (y.kind().isConstant()) {
                putF1(code, op + 16, x.regNo(), x.regNo(), y.value(), null);
            } else {
                load(code, y);
                putF1(code, op, x.regNo(), x.regNo(), y.regNo(), null);
                RegisterAllocator.deallocate(y.regNo());
            }
        }
    }



    /*
     * @params - x is a subtree (result object)
     * Puts the subtree's value in register and updated x will be a register Kind
     */
    public static void load(Code code, Result x) {
        if (x.kind().isRegister()) {
            return;
        }
        int regNo = RegisterAllocator.allocateReg();
        if (x.kind().isConstant()) {
            putF1(code, OperationCode.ADDI, regNo, 0, x.value(), null);
            x.kind(Kind.REG);
            x.regNo(regNo);
        } else if (x.kind().isVariable()) {
            //$TODO$ Implement variable space along with Framepointer
            // Offset Hard coded to 0. Should point to the actual Framepointer
            final Symbol symbol = new Symbol(x.getVariableName(), code.getPc(), SymbolType.VARIABLE, true, x.address());
            symbol.setResult(x);
            putF1(code, OperationCode.LDW, regNo, 0, x.address(), symbol);
            x.kind(Kind.REG);
            x.regNo(regNo);
        }
    }

    public static void declareSymbol(String symbolName, SymbolTable symbolTable, SymbolType type, List<Integer> arrayDimensions) {
        final Symbol s = new Symbol(symbolName, -1, type, false, null);
        if (type == SymbolType.ARRAY) {
            final int dimensionCount = arrayDimensions.size();
            int[] dimensionsArray = new int[dimensionCount];
            for (int i = 0; i < arrayDimensions.size(); i++) {
                dimensionsArray[i] = arrayDimensions.get(i);
            }
            s.setArrayDimension(dimensionCount);
            s.setValue(Array.newInstance(Integer.class, dimensionsArray));
        }
        symbolTable.addSymbol(s);
    }


    public static void main(String[] args) {
        final Code code = new Code();
        AuxiliaryFunctions.putF1(code, OperationCode.ADD, 1, 2, 5, null);
//        Printer.print(code.toString());
    }

    public static void removeInstruction(Code code, BasicBlock node) {
        final List<Instruction> instructions = node.getInstructions();
        final List<Instruction> remove = new ArrayList<Instruction>();
        for (Instruction instruction : instructions) {
            if(instruction.isPhi()) {
                PhiInstruction phi = (PhiInstruction) instruction;
                if(phi.canIgnore()) {
                    remove.add(instruction);
                }
            }
        }
        for (Instruction instruction : remove) {
            code.removeCode(instruction);
            node.getInstructions().remove(instruction);
            node.removePhiInstruction(instruction.getSymbol().getName());
        }
    }
}
