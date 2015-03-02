package com.acc.structure;

import com.acc.data.Instruction;

/**
 * Created by prabhuk on 2/8/2015.
 */
public class ControlFlowGraph {
    private BasicBlock currentBlock;
    private final BasicBlock rootBlock;

    public ControlFlowGraph() {
        rootBlock = new BasicBlock();
        currentBlock = rootBlock;
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
