package com.acc.util;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.PhiInstruction;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by prabhuk on 2/18/2015.
 */
public class PhiInstructionHelper {

    public static void createPhiInstructions(SymbolTable table, BasicBlock join, Code code) {

        handleLeft(join, table, code);
        handleRight(join, table, code);
        fillIncomplete(join, table);
        final List<Instruction> instructions = join.getInstructions();
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
            join.getInstructions().remove(instruction);
            join.removePhiInstruction(instruction.getSymbol().getName());
        }
    }

    private static void fillIncomplete(BasicBlock join, SymbolTable table) {
        //Handle Phi Statements where right did not have assignments
        final Collection<Instruction> allPhiInstructions = join.getAllPhiInstructions();
        for (Instruction allPhiInstruction : allPhiInstructions) {
            final PhiInstruction phi = (PhiInstruction) allPhiInstruction;
            if (!phi.isComplete()) {
                Symbol targetSymbol = getTargetSymbol(table, phi.getSymbol());
                if(phi.getLeftSymbol() == null) {
                    phi.setLeftSymbol(targetSymbol);
                } else {
                    phi.setRightSymbol(targetSymbol);
                }
            }
        }
    }

    private static void handleRight(BasicBlock join, SymbolTable table, Code code) {
        final BasicBlock right = join.getRight();
        final List<Instruction> rightInstructions = right.getInstructions();
        for (Instruction instruction : rightInstructions) {
            if (instruction.getOpcode() == OperationCode.MOV || instruction.getOpcode() == OperationCode.PHI) {
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
            if (instruction.getOpcode() == OperationCode.MOV || instruction.getOpcode() == OperationCode.PHI) {
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
        final Symbol phiSymbol = new Symbol(targetSymbol.getName(), targetSymbol.getSuffix(), targetSymbol.getType(), targetSymbol.isPointerValue(), Symbol.cloneValue(targetSymbol.getValue()));
        PhiInstruction phi = new PhiInstruction(phiSymbol);
        code.addPhiInstruction(phi);//$TODO$ this is not in order but should generate an unique suffix
        join.addPhiInstruction(phi);
        phiSymbol.setSuffix(phi.getLocation());
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