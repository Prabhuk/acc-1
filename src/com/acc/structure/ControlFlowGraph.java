package com.acc.structure;

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
        allBlocks.add(rootBlock);
    }

    /**
     * @param instruction
     */
    public void addInstruction(Instruction instruction) {
        currentBlock.addInstruction(instruction);
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
