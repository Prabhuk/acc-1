package com.acc.util;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;

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
        AuxiliaryFunctions.removeInstruction(code, join);
    }

    private static void fillIncomplete(BasicBlock join, SymbolTable table) {
        //Handle Phi Statements where right did not have assignments
        final Collection<Instruction> allPhiInstructions = join.getAllPhiInstructions();
        for (Instruction phi : allPhiInstructions) {
            if (!phi.isComplete()) {
                Symbol targetSymbol = table.getTargetSymbol(phi.getSymbol());
                final Result targetResult = new Result(targetSymbol);
                if(phi.getX() == null) {
                    phi.setX(targetResult);
                } else {
                    phi.setY(targetResult);
                }
            }
        }
    }



    private static void handleRight(BasicBlock join, SymbolTable table, Code code) {
        final BasicBlock right = join.getRight();
        final List<Instruction> rightInstructions = right.getInstructions();
        for (Instruction instruction : rightInstructions) {
            if (instruction.getOpcode() == OperationCode.move || instruction.getOpcode() == OperationCode.phi) {
                final Symbol symbol = instruction.getSymbol();
                if (join.getPhiInstruction(symbol.getName()) == null) {
                    //Processed left so expecting the symbol to be present in the currentSymbolTable
                    createPhi(join, table, code, symbol);
                }
                //If there is an NPE in the next line then the variable is not declared in the scope
                final Instruction phi = join.getPhiInstruction(symbol.getName());
                phi.setY(new Result(instruction.getSymbol()));
            }
        }
    }

    private static void handleLeft(BasicBlock join, SymbolTable table, Code code) {
        final BasicBlock left = join.getLeft();
        final List<Instruction> leftInstructions = left.getInstructions();
        for (Instruction instruction : leftInstructions) {
            if (instruction.getOpcode() == OperationCode.move || instruction.getOpcode() == OperationCode.phi) {
                final Symbol symbol = instruction.getSymbol();
                Instruction phi;
                if (join.getPhiInstruction(symbol.getName()) != null) {
                    //Update the existing phi instruction
                    phi = join.getPhiInstruction(symbol.getName());
                } else {
                    phi = createPhi(join, table, code, symbol);
                }
                phi.setX(new Result(symbol));
            }
        }
    }

    private static Instruction createPhi(BasicBlock join, SymbolTable table, Code code, Symbol symbol) {
        Symbol targetSymbol = table.getTargetSymbol(symbol);
        final Symbol phiSymbol = new Symbol(targetSymbol.getName(), targetSymbol.getSuffix(), targetSymbol.getValue());
        Instruction phi = new Instruction(OperationCode.phi, null, null, code.getPc());
        phi.setSymbol(phiSymbol);

        final Instruction instruction = join.addPhiInstruction(phi);
        if(instruction != null) {
            code.addCode(phi, instruction);
            //this is make sure the order of instructions are maintained in the flat list of instructions within Code
        } else {
            code.addCode(phi);
        }
        phiSymbol.setSuffix(phi.getLocation());
        return phi;
    }
}
