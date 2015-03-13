package com.acc.util;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.graph.GraphHelper;
import com.acc.graph.MoveFinder;
import com.acc.graph.PhiOperandFinder;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 2/18/2015.
 */
public class PhiInstructionHelper {

    private static List<Instruction> phis;

    public static void createPhiInstructions(SymbolTable table, BasicBlock join, Code code, BasicBlock start) {
        phis = new ArrayList<Instruction>();
        handleLeft(start, join, table, code);
        handleRight(start, join, table, code);
        fillIncomplete(code, join, table);
    }

    private static boolean equals(Result x, Result y) {
        if (x.kind() == y.kind()) {
            if (x.isVariable() && y.isVariable()) {
                return x.getUniqueIdentifier().equals(y.getUniqueIdentifier());
            }
            if (x.isConstant() && y.isConstant()) {
                return x.value().equals(y.value());
            }

            if (x.isIntermediate() && y.isIntermediate()) {
                return x.getIntermediateLoation().equals(y.getIntermediateLoation());
            }
        }
        return false;
    }

    private static void fillIncomplete(Code code, BasicBlock join, SymbolTable table) {
        //Handle Phi Statements where right did not have assignments
        final Collection<Instruction> allPhiInstructions = phis;
        int locationOffset = 0;
        for (Instruction phi : allPhiInstructions) {
            if (!phi.isComplete()) {
                final PhiOperandFinder operandFinder = new PhiOperandFinder(null, join, phi.getSymbol().getName());
                new GraphHelper(operandFinder, code.getControlFlowGraph().getRootBlock());
                final Result operand = operandFinder.getOperand();
                if (phi.getX() == null) {
                    if (!equals(operand, phi.getY())) {
                        phi.setX(operand);
                    }
                } else if (phi.getY() == null) {
                    if (!equals(operand, phi.getX())) {
                        phi.setY(operand);
                    }
                }
            }

            if (phi.isComplete()) {
                final Instruction instruction = join.addPhiInstruction(phi);
                if (instruction != null) {
                    code.addCode(phi, instruction);
                    //this is make sure the order of instructions are maintained in the flat list of instructions within Code
                } else {
                    code.addCode(phi);
                }
                phi.getSymbol().setSuffix(phi.getLocation());
            }
        }
    }

    private static void fillWhilePhiIncomplete(Code code, BasicBlock join, SymbolTable table) {
        //Handle Phi Statements where right did not have assignments
        final Collection<Instruction> allPhiInstructions = phis;
        int locationOffset = 0;
        for (Instruction phi : allPhiInstructions) {
            if (!phi.isComplete()) {
                final PhiOperandFinder operandFinder = new PhiOperandFinder(null, join, phi.getSymbol().getName());
                new GraphHelper(operandFinder, code.getControlFlowGraph().getRootBlock());
                final Result operand = operandFinder.getOperand();
                if (phi.getX() == null) {
                    phi.setX(operand);
                }
            }

            if (phi.isComplete()) {
                final Instruction instruction = join.addPhiInstruction(phi);
                if (instruction != null) {
                    code.addCode(phi, instruction);
                    //this is make sure the order of instructions are maintained in the flat list of instructions within Code
                } else {
                    code.addCode(phi);
                }
                phi.getSymbol().setSuffix(phi.getLocation());
            }
        }
    }


    private static void handleRight(BasicBlock start, BasicBlock join, SymbolTable table, Code code) {
        final MoveFinder operandFinder = new MoveFinder(null, join, start);
        new GraphHelper(operandFinder, code.getControlFlowGraph().getRootBlock());
        final List<Instruction> rightInstructions = operandFinder.getMoves();
        ;
        for (Instruction instruction : rightInstructions) {
            if (instruction.getOpcode() == OperationCode.move || instruction.getOpcode() == OperationCode.phi) {
                final Symbol symbol = instruction.getSymbol();
                //If there is an NPE in the next line then the variable is not declared in the scope
                Instruction phi = findPhi(symbol, join);
                if (phi == null) {
                    //Processed left so expecting the symbol to be present in the currentSymbolTable
                    createPhi(join, table, code, symbol);
                    phi = findPhi(symbol, join);
                }
                if (phi != null) {
                    phi.setY(new Result(instruction.getSymbol()));
                }
            }
        }
    }

    private static Instruction findPhi(Symbol symbol, BasicBlock join) {
        if (join.getPhiInstruction(symbol.getName()) != null) {
            return join.getPhiInstruction(symbol.getName());
        }
        for (Instruction phi1 : phis) {
            if (phi1.getSymbol().getName().equals(symbol.getName())) {
                return phi1;
            }
        }
        return null;
    }

    private static void handleLeft(BasicBlock start, BasicBlock join, SymbolTable table, Code code) {
        final MoveFinder operandFinder = new MoveFinder(null, join, start);
        new GraphHelper(operandFinder, code.getControlFlowGraph().getRootBlock());
        final List<Instruction> moves = operandFinder.getMoves();
        for (Instruction move : moves) {
            final Symbol symbol = move.getSymbol();
            Instruction phi = findPhi(symbol, join);
            if (phi == null) {
                createPhi(join, table, code, symbol);
                phi = findPhi(symbol, join);
            }
            if (move.isPhi()) {
                Result x = new Result(Kind.INTERMEDIATE);
                x.setIntermediateLoation(move.getLocation());
                phi.setX(x);
            } else {
                phi.setX(new Result(symbol));
            }
        }
    }

    private static Instruction createPhi(BasicBlock join, SymbolTable table, Code code, Symbol symbol) {
        Symbol targetSymbol = table.getTargetSymbol(symbol);
        final Symbol phiSymbol = new Symbol(targetSymbol.getName(), targetSymbol.getSuffix(), targetSymbol.getValue());
        Instruction phi = new Instruction(OperationCode.phi, null, null, code.getPc());
        phi.setSymbol(phiSymbol);
        if (!phis.contains(phi)) {
            phis.add(phi);
        }
        return phi;
    }

    private static void oldHandleLeft(BasicBlock join, SymbolTable table, Code code) {
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

    public static void createPhiInstructions(Code code, BasicBlock currentBlock, SymbolTable symbolTable) {
        if (currentBlock.isWhileHead()) {
            phis = new ArrayList<Instruction>();
            final Set<BasicBlock> children = currentBlock.getChildren();
            BasicBlock right = null;
            for (BasicBlock child : children) {
                if (!child.equals(currentBlock)) {
                    right = child;
                    break;
                }
            }

            final BasicBlock join = currentBlock.getJoinBlock();
            oldHandleLeft(join, symbolTable, code);
            handleRight(right, join, symbolTable, code);
            fillWhilePhiIncomplete(code, join, symbolTable);
        }
    }

    public static void createPhiForIF(Code code, BasicBlock currentBlock, SymbolTable symbolTable) {
        phis = new ArrayList<Instruction>();
        final Set<BasicBlock> children = currentBlock.getChildren();
        BasicBlock right = null;
        final BasicBlock join = currentBlock.getJoinBlock();
        BasicBlock left = null;
        for (BasicBlock child : children) {
            if(left == null) {
                left = child;
            } else {
                right = child;
            }
        }
        handleLeft(left, join, symbolTable, code);
        handleRight(right, join, symbolTable, code);
        fillIncomplete(code, join, symbolTable);
    }

}
