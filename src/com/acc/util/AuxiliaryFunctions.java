package com.acc.util;

import com.acc.constants.Condition;
import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.PhiInstruction;
import com.acc.data.Result;
import com.acc.structure.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * The auxilary methods to cre
 */
public class AuxiliaryFunctions {
    private static final List<Register> registers = new ArrayList<Register>(32);

    static {
        //Create registers
        for(int i =0; i<32; i++) {
            registers.add(new Register());
        }
    }

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
        putF1(code, OperationCode.BEQ, 0, 0, loc, null, null);
    }

    public static void FJLink(Code code, Result x) {
        putF1(code, OperationCode.BEQ, 0, 0, x.fixupLoc(), null, null);
        x.fixupLoc(code.getPc() - 1);
    }

    public static void CJF(Code code, Result x) {
        //OperationCode.BEQ + $TODO$ WTF
        putF1(code, Condition.getNegatedInstruction(x.condition()), x.regNo(), 0, 0, null, null);
        deallocate(x.regNo());
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
        registers.get(regno).setAvailable(true);
        registers.get(regno).setValue(null);
        //$TODO$ needs implementation
    }

    /*
     * @params - x is a subtree (result object)
     * Puts the subtree's value in register and updated x will be a register Kind
     */
    public static void load(Code code, Result x) {
        if (x.kind().isRegister()) {
            return;
        }
        int regNo = allocateReg();
        if (x.kind().isConstant()) {
            putF1(code, OperationCode.ADDI, regNo, 0, x.value(), null, null);
            x.kind(Kind.REG);
            x.regNo(regNo);
        } else if (x.kind().isVariable()) {
            //$TODO$ Implement variable space along with Framepointer
            // Offset Hard coded to 0. Should point to the actual Framepointer
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

    public static int allocateReg() {
        for (Register register : registers) {
            if(register.isAvailable()) {
                register.setAvailable(false);
                return registers.indexOf(register);
            }
        }
        throw new RuntimeException("Registers are full");
    }

    public static void main(String[] args) {
        final Code code = new Code();
        AuxiliaryFunctions.putF1(code, OperationCode.ADD, 1, 2, 5, null, null);
//        Printer.print(code.toString());
    }

    public static void createPhiInstructions(SymbolTable table, BasicBlock join, Code code) {
        handleLeft(join, table, code);
        handleRight(join, table, code);
        fillincompleteRight(join, table);
    }

    private static void fillincompleteRight(BasicBlock join, SymbolTable table) {
        //Handle Phi Statements where right did not have assignments
        final Collection<Instruction> allPhiInstructions = join.getAllPhiInstructions();
        for (Instruction allPhiInstruction : allPhiInstructions) {
            final PhiInstruction phi = (PhiInstruction) allPhiInstruction;
            if (!phi.isComplete()) {
                Symbol targetSymbol = getTargetSymbol(table, phi.getSymbol());
                phi.setRightSymbol(targetSymbol);
            }
        }
    }

    private static void handleRight(BasicBlock join, SymbolTable table, Code code) {
        final BasicBlock right = join.getRight();
        final List<Instruction> rightInstructions = right.getInstructions();
        for (Instruction instruction : rightInstructions) {
            if (instruction.getOpcode() == OperationCode.MOV) {
                final Symbol symbol = instruction.getSymbol();
                if (join.getPhiInstruction(symbol.getName()) == null) {
                    //Processed left so expecting the symbol to be present in the currentSymbolTable
                    createPhi(join, table, code, symbol);
                }
                //If there is an NPE in the next line then the variable is not declared in the scope
                final PhiInstruction phi = (PhiInstruction) join.getPhiInstruction(symbol.getName());
                phi.setRightSymbol(instruction.getSymbol());
            }
        }
    }

    private static void handleLeft(BasicBlock join, SymbolTable table, Code code) {
        final BasicBlock left = join.getLeft();
        final List<Instruction> leftInstructions = left.getInstructions();
        for (Instruction instruction : leftInstructions) {
            if (instruction.getOpcode() == OperationCode.MOV) {
                final Symbol symbol = instruction.getSymbol();
                PhiInstruction phi;
                if (join.getPhiInstruction(symbol.getName()) != null) {
                    //Update the existing phi instruction
                    phi = (PhiInstruction) join.getPhiInstruction(symbol.getName());
                } else {
                    phi = createPhi(join, table, code, symbol);
                }
                phi.setLeftSymbol(symbol);
            }
        }
    }

    private static PhiInstruction createPhi(BasicBlock join, SymbolTable table, Code code, Symbol symbol) {
        Symbol targetSymbol = getTargetSymbol(table, symbol);
        PhiInstruction phi = new PhiInstruction(targetSymbol);
        code.addPhiInstruction(phi);//$TODO$ this is not in order but should generate an unique suffix
        join.addPhiInstruction(phi);
        return phi;
    }

    private static Symbol getTargetSymbol(SymbolTable table, Symbol symbol) {
        final List<Symbol> symbols = table.getSymbols();
        Symbol targetSymbol = null;
        for (Symbol symbol1 : symbols) {
            if (symbol1.getName().equals(symbol.getName())) {
                targetSymbol = symbol1;
            }
        }
        return targetSymbol;
    }
}
