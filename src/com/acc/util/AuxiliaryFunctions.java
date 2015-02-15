package com.acc.util;

import com.acc.constants.Condition;
import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.structure.SymbolType;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * The auxilary methods to cre
 */
public class AuxiliaryFunctions {

    public static void putF1(Code code, int instructionCode, int a, int b, int c, Symbol symbol, Result rhs) {
        if (c < 0) c ^= 0xFFFF0000;
        final int ins = instructionCode << 26 | a << 21 | b << 16 | c;
        final Instruction instruction = new Instruction(ins, instructionCode, a, b, c, symbol, rhs);
        code.addCode(instruction);
    }

    public static void putF2(Code code, int instructionCode, int a, int b, int c) {

        final int ins = instructionCode << 26 | a << 21 | b << 16 | c;
        final Instruction instruction = new Instruction(ins, instructionCode, a, b, c, null, null);
        code.addCode(instruction);
    }

    public static void putF3(Code code, int instructionCode, int c) {
        final int ins = instructionCode << 26 | c;
        final Instruction instruction = new Instruction(ins, instructionCode, null, null, c, null, null);
        code.addCode(instruction);
    }

    public static void BJ(Code code, int loc, BasicBlock loopBlock) {
        putF1(code, OperationCode.BEQ, 0, 0, loc - code.getPc(), null, null);
        code.processJoins(loopBlock);
    }

    public static void FJLink(Code code, Result x) {
        putF1(code, OperationCode.BEQ, 0, 0, x.fixupLoc(), null, null);
        x.fixupLoc(code.getPc() - 1);
    }

    public static void CJF(Code code, Result x) {
        putF1(code, OperationCode.BEQ + Condition.getNegatedInstruction(x.condition()), x.regNo(), 0, 0, null, null);
        x.fixupLoc(code.getPc() - 1);
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
                putF1(code, op + 16, x.regNo(), x.regNo(), y.value(), null, null);
            } else {
                load(code, y);
                putF1(code, op, x.regNo(), x.regNo(), y.regNo(), null, null);
                deallocate(y.regNo());
            }
        }
    }

    private static void deallocate(int regno) {
        //$TODO$ needs implementation
    }

    /*
     * @params - x is a subtree (result object)
     * Puts the subtree's value in register and updated x will be a register Kind
     */
    private static void load(Code code, Result x) {
        if (x.kind().isRegister()) {
            return;
        }
        int regNo = allocateReg();
        if (x.kind().isConstant()) {
            putF1(code, OperationCode.ADDI, regNo, 0, x.value(), null, null);
            x.kind(Kind.REG);
            x.regNo(regNo);
        } else if (x.kind().isVariable()) {
            //$TODO$ Frame pointer doesn't make any sense to our design as of now or doesn't make any sense to me :P
            putF1(code, OperationCode.LDW, regNo, 0, x.address(), null, null);
            x.kind(Kind.REG);
            x.regNo(regNo);
        }
    }

    public static boolean assignToSymbol(Code code, String symbolName, Result rhs, SymbolTable symbolTable) {
        if (rhs.kind().isConstant()) {
            final int moveInstructionNumber = code.getPc();
            final Symbol symbol = new Symbol(symbolName, moveInstructionNumber,
                    SymbolType.VARIABLE, false, Symbol.cloneValue(rhs.value()));
            AuxiliaryFunctions.putF1(code, OperationCode.MOV, symbolTable.getFramePointer(),
                    rhs.value(), OperationCode.MOV_CONSTANT, symbol, rhs);
            symbolTable.addSymbol(symbol);
        } else if (rhs.kind().isVariable()) {
            //$TODO$ Expression should set address as the framepointer value of the variable in the symboltable
            final int moveInstructionNumber = code.getPc();
            final Symbol symbol = new Symbol(symbolName, moveInstructionNumber,
                    SymbolType.VARIABLE, true, Symbol.cloneValue(rhs.value()));
            AuxiliaryFunctions.putF1(code, OperationCode.MOV,
                    symbolTable.getFramePointer(), rhs.address(), OperationCode.MOV_VARIABLE, symbol, rhs);
            symbolTable.addSymbol(symbol);
        } else {
            return false;
        }
        return true;
    }

    public static void declareSymbol(Code code, String symbolName, SymbolTable symbolTable, SymbolType type, List<Integer> arrayDimensions) {
        final Symbol s = new Symbol(symbolName, -1, type, false, null);
        if(type == SymbolType.ARRAY) {
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

    private static int allocateReg() {
        //$TODO$ pending implementation
        return 0;
    }

    public static void main(String[] args) {
        final Code code = new Code();
        AuxiliaryFunctions.putF1(code, OperationCode.ADD, 1, 2, 5, null, null);
//        Printer.print(code.toString());
    }
}
