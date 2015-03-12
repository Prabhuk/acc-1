package com.acc.util;

import com.acc.constants.Condition;
import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.structure.SymbolType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * The auxilary methods to cre
 */
public class AuxiliaryFunctions {

    public static void BJ(Code code, int loc) {
        final Result x = new Result(Kind.CONSTANT);
        x.value(loc);
        addInstruction(OperationCode.bra, code, x, null, null);
    }

    public static void FJLink(Code code, Result follow) {
//        final Symbol symbol = new Symbol(String.valueOf(x.fixupLoc()), code.getPc(), null, false, x.fixupLoc());
//        symbol.setResult(x);
        Result equal = new Result(Kind.CONSTANT);
        equal.value(0);
        Result branchLocation = new Result(Kind.CONSTANT);
        branchLocation.value(0);
        addInstruction(OperationCode.beq, code, equal, branchLocation, null);
        follow.fixupLoc(code.getPc() - 1);
    }

    public static void CJF(Code code, Result currentState, SymbolTable symbolTable) {
        Result x = new Result(Kind.INTERMEDIATE);
        x.setIntermediateLoation(code.getPc() - 1);
        Result y = new Result(Kind.CONSTANT);
        y.value(0);
        AuxiliaryFunctions.addInstruction(Condition.getNegatedInstruction(currentState.condition()), code, x, y, symbolTable);
        currentState.fixupLoc(code.getPc() - 1);
    }


    /*
     * Combines x & y and resultant result obj is maintained in x.
     */
    public static void combine(Code code, String op, Result x, Result y) {
        if (x.isConstant() && y.isConstant()) {
            if (op.equals(OperationCode.add)) {
                x.value(x.value() + y.value());
            } else if (op.equals(OperationCode.sub)) {
                x.value(x.value() - y.value());
            } else if (op.equals(OperationCode.mul)) {
                x.value(x.value() * y.value());
            } else if (op.equals(OperationCode.div)) {
                x.value(x.value() / y.value());
            } else {
                throw new UnsupportedOperationException("Combine cannot process Operation code [" + op + "]");
            }
        }
    }


    public static void declareSymbol(String symbolName, SymbolTable symbolTable, SymbolType type, List<Integer> arrayDimensions) {
        final Symbol s;
        if (type == SymbolType.ARRAY) {
            List<Result> originalArrayIdentifiers = new ArrayList<Result>();
            s = new Symbol(symbolName, -1, arrayDimensions.size(), null);
            final int dimensionCount = arrayDimensions.size();
            int[] dimensionsArray = new int[dimensionCount];
            for (int i = 0; i < arrayDimensions.size(); i++) {
                dimensionsArray[i] = arrayDimensions.get(i);
                final Result identifier = new Result(Kind.CONSTANT);
                identifier.value(arrayDimensions.get(i));
                originalArrayIdentifiers.add(identifier);
            }
            s.setArrayDimension(dimensionCount);
            s.setArrayIdentifiers(originalArrayIdentifiers);
//            s.setArrayValue(Array.newInstance(Integer.class, dimensionsArray));
        } else {
            s = new Symbol(symbolName, -1, null, type);
        }
        symbolTable.addSymbol(s);
    }

    public static Instruction addInstruction(int op, Code code, Result x, Result y, SymbolTable symbolTable) {
        return addInstruction(op, code, x, y, symbolTable, -1);
    }

    public static Instruction addInstruction(int op, Code code, Result x, Result y, SymbolTable symbolTable, int index) {
        final Instruction instruction = new Instruction(op, x, y, code.getPc());
        if (OperationCode.getOperandCount(op) > 0) {
            if (symbolTable != null && (x.isVariable() || x.isArray())) {
                Symbol recent = symbolTable.getRecentOccurence(x.getVariableName());
                if (x.isArray()) {
                    if (recent.getSuffix() == -1) {
                        instruction.setSymbol(recent);
                        //Making sure arrays have only one entry in symbol table besides the declaration
                    }
                } else {
                    instruction.setSymbol(recent);
                }
            }
        }
        code.addCode(instruction, index);
        return instruction;
    }

    public static void addMoveInstruction(Code code, Result x, Result y, SymbolTable symbolTable) {
        addToSymbolTable(code, symbolTable, x);
        x.setLocation(code.getPc());
        if(y.isVariable() && y.getLocation() == null) {
            Printer.debugMessage("Variable without location in Move");
            final Symbol recentOccurence = symbolTable.getRecentOccurence(y.getVariableName());
            y.setLocation(recentOccurence.getSuffix());
        }
        addInstruction(OperationCode.move, code, x, y, symbolTable);
    }

    private static void addToSymbolTable(Code code, SymbolTable symbolTable, Result x) {
        Symbol recent = symbolTable.getRecentOccurence(x.getVariableName());
        final Symbol symbol;
        if (recent.getType().isArray()) {
            symbol = new Symbol(recent.getName(), code.getPc(), recent.getArrayDimension(), recent.getValue());
        } else {
            symbol = new Symbol(recent.getName(), code.getPc(), recent.getValue());
        }
        symbolTable.addSymbol(symbol);
    }

    /**
     * May or may not add the kill instruction to the code:
     * Added ONLY if there is a join block
     * @param code
     * @param recent
     * @return Program Counter
     */
    public static int addKillInstruction(Code code, Symbol recent) {
        return code.addCode(new Instruction(OperationCode.kill, new Result(recent), null, code.getPc()));
    }
}
