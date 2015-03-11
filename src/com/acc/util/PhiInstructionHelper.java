package com.acc.util;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
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

    private static List<Instruction> phis;

    public static void createPhiInstructions(SymbolTable table, BasicBlock join, Code code) {
        phis = new ArrayList<Instruction>();
        handleLeft(join, table, code);
        handleRight(join, table, code);
        fillIncomplete(code, join, table);
    }

    private static void fillIncomplete(Code code, BasicBlock join, SymbolTable table) {
        //Handle Phi Statements where right did not have assignments
        final Collection<Instruction> allPhiInstructions = phis;
        int locationOffset = 0;
        for (Instruction phi : allPhiInstructions) {
            if (phi.isComplete()) {
                final Instruction instruction = join.addPhiInstruction(phi);
                if(instruction != null) {
                    code.addCode(phi, instruction);
                    //this is make sure the order of instructions are maintained in the flat list of instructions within Code
                } else {
                    code.addCode(phi);
                }
                phi.getSymbol().setSuffix(phi.getLocation());

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
                Instruction phi = join.getPhiInstruction(symbol.getName());
                if(phi == null) {
                    for (Instruction phi1 : phis) {
                        if(phi1.getSymbol().getName().equals(symbol.getName())) {
                            phi = phi1;
                            break;
                        }
                    }
                }
                if(phi != null) {
                    phi.setY(new Result(instruction.getSymbol()));
                }
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
        if(!phis.contains(phi)) {
            phis.add(phi);
        }
        return phi;
    }
}
