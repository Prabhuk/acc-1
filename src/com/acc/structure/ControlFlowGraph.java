package com.acc.structure;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.util.AuxiliaryFunctions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 2/8/2015.
 */
public class ControlFlowGraph {
    private final List<BasicBlock> allBlocks = new LinkedList<BasicBlock>();
    private BasicBlock currentBlock;
    private final BasicBlock rootBlock;
    private static volatile ControlFlowGraph tree;

    public static ControlFlowGraph getDominatorTree() {
        synchronized (ControlFlowGraph.class) {
            if (tree == null) {
                tree = new ControlFlowGraph();
            }
        }
        return tree;
    }

    private ControlFlowGraph() {
        rootBlock = new BasicBlock();
        currentBlock = rootBlock;
        allBlocks.add(rootBlock);
    }

    /**
     * @param instruction
     * @param code
     */
    public void addInstruction(Instruction instruction, Code code) {
        final int opcode = instruction.getOpcode();
        currentBlock.addInstruction(instruction);
        if (opcode >= OperationCode.BEQ && opcode <= OperationCode.RET) {
            addNewCurrentBlock();
        }

        if (opcode == OperationCode.MOV) {
            final Instruction phiInstruction = new Instruction(OperationCode.PHI, "phi ");
            //$TODO$ generate proper code for PHI
            //$TODO$ ONLY ONE PHI PER SYMBOL. So run update if its the same symbol
//$TODO$ This generation of Phi needs to use symbol table
            code.addCode(phiInstruction);
            currentBlock.getJoinBlock().addInstruction(phiInstruction);
        }
    }

    private void addNewCurrentBlock() {
        final BasicBlock basicBlock = new BasicBlock();
        currentBlock.addDominatedOverBlock(basicBlock);
        allBlocks.add(basicBlock);
        for (BasicBlock block : allBlocks) {
            if (block.getDominatesOver().contains(currentBlock)) {
                block.addDominatedOverBlock(basicBlock);
            }
        }
        currentBlock = basicBlock;
    }

    public void processLoopJoinBlock(BasicBlock loopBlock, Code code) {
        final BasicBlock joinBlock = currentBlock.getJoinBlock();
        final List<Instruction> instructions = joinBlock.getInstructions();
        for (int i = instructions.size() - 1; i >= 0; i--) {
            loopBlock.getInstructions().add(0, instructions.get(i));
        }

        updateParentPHIs(loopBlock, code);
        addNewCurrentBlock();
    }

    private void updateParentPHIs(BasicBlock loopBlock, Code code) {
        for (BasicBlock b : loopBlock.getParents()) {
            if(b.getDominatesOver().contains(loopBlock)) {
                final BasicBlock joinBlock = b.getJoinBlock();
                final List<Instruction> instructions = joinBlock.getInstructions();
                List<Instruction> existingPhis = new ArrayList<Instruction>();
                for (Instruction instruction : instructions) {
                    if(instruction.isPhi()) {
                        existingPhis.add(instruction);
                        final Instruction phiInstruction = new Instruction(OperationCode.PHI, "phi "); //$TODO$ Need to create a proper phi with instruction numbers
                        //$TODO$ This wont be part of list<instructions> in code for now.
                        //$TODO$ This generation of Phi needs to use symbol table
                        b.getJoinBlock().addInstruction(phiInstruction);
                    }
                }
            }
        }
    }

    public void processIFELSEJoinBlock(Code code) {
        currentBlock = currentBlock.getJoinBlock();
        allBlocks.add(currentBlock);
        updateParentPHIs(currentBlock, code);
        addNewCurrentBlock();
    }

    public BasicBlock getCurrentBlock() {
        return currentBlock;
    }
}
