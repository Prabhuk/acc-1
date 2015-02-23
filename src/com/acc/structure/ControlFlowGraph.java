package com.acc.structure;

import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.SSAInstruction;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 2/8/2015.
 */
public class ControlFlowGraph {
    private BasicBlock currentBlock;
    private final BasicBlock rootBlock;
    private static volatile ControlFlowGraph tree;

    public static ControlFlowGraph getCFG() {
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
    }

    /**
     * @param instruction
     */
    public void addInstruction(Instruction instruction) {
        currentBlock.addInstruction(instruction);
    }


    public void addSSAInstruction(SSAInstruction instruction) {
        currentBlock.addSSAInstruction(instruction);
    }

    public BasicBlock getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(BasicBlock block) {
        currentBlock = block;
    }

    public BasicBlock getRootBlock() {
        return rootBlock;
    }

}
