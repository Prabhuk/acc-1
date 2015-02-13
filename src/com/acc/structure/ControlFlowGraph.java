package com.acc.structure;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 2/8/2015.
 */
public class ControlFlowGraph {
    private final List<BasicBlock> allBlocks = new LinkedList<BasicBlock>();
    private BasicBlock currentBlock;
    private BasicBlock joinBlock;
    private final BasicBlock rootBlock;
    private static volatile ControlFlowGraph tree;

    public static ControlFlowGraph getDominatorTree() {
        synchronized (ControlFlowGraph.class) {
            if(tree == null) {
                tree = new ControlFlowGraph();
            }
        }
        return tree;
    }
    private ControlFlowGraph() {
        rootBlock =  new BasicBlock();
        currentBlock = rootBlock;
        allBlocks.add(rootBlock);
    }

    /**
     * @param instruction
     * @param code
     */
    public void addInstruction(Instruction instruction, Code code) {
        final int opcode = instruction.getOpcode();
        currentBlock.addToBlock(instruction);
        if(opcode >= OperationCode.BEQ && opcode <= OperationCode.RET) {
            final BasicBlock basicBlock = new BasicBlock();
            currentBlock.addDominatedOverBlock(basicBlock);

            allBlocks.add(basicBlock);
            for (BasicBlock block : allBlocks) {
                if(block.getDominatesOver().contains(currentBlock)) {
                    block.addDominatedOverBlock(basicBlock);
                }
            }
            currentBlock = basicBlock;
            joinBlock = new BasicBlock();
        }

        if(opcode == OperationCode.MOV) {
            final Instruction phiInstruction = new Instruction(OperationCode.PHI, "phi "); //$TODO$ generate proper code for PHI
            code.addCode(phiInstruction);
            joinBlock.addToBlock(phiInstruction);
        }
    }

    public void processJoin() {
        if(joinBlock != null) {
            //$TODO$ figure out who calls process Join. (Fixup, bj etc.) Distinguish if and while here
        }
    }

    public BasicBlock getJoinBlock() {
        return joinBlock;
    }

    public void setJoinBlock(BasicBlock joinBlock) {
        this.joinBlock = joinBlock;
    }
}
