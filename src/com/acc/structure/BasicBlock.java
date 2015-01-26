package com.acc.structure;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 1/25/2015.
 * Datastructure to store the instructions in a single basicBlock
 */
public class BasicBlock {
    private final List<Integer> block = new LinkedList<Integer>();
    private final Set<BasicBlock> dominators = new HashSet<BasicBlock>();

    public Set<BasicBlock> getDominators() {
        return dominators;
    }

    public List<Integer> getBlock() {
        return block;
    }

    /*
     * Adds an instruction to the basicBlock
     */
    public void addToBlock(Integer instruction) {
        block.add(instruction);
    }

    /*
     * Removes the given instruction from the basicBlock if it already exists.
     * Returns true on successful removal and false otherwise
     */
    public boolean removeFromBlock(Integer instruction) {
        return block.remove(instruction);
    }


    /*
     * returns true if the block parameter is a dominating block for the current block
     */
    public boolean isDominatingBlock(BasicBlock block) {
        return dominators.contains(block);
    }

    /*
     * The block parameter will be added as a dominating block for the current block
     */
    public void addDominatingBlock(BasicBlock block) {
        dominators.add(block);
    }

}
