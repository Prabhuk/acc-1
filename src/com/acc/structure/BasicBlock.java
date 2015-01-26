package com.acc.structure;

import java.util.*;

/**
 * Created by prabhuk on 1/25/2015.
 * Datastructure to store the instructions in a single basicBlock
 */
public class BasicBlock {
    /*
     * Storing the instruction number along with the
     */
    private final Map<Integer, Integer> block = new LinkedHashMap<Integer, Integer>();
    private final Set<BasicBlock> dominators = new HashSet<BasicBlock>();

    public Set<BasicBlock> getDominators() {
        return dominators;
    }

    public Map<Integer, Integer> getBlock() {
        return block;
    }

    /*
     * Adds an instruction to the basicBlock
     */
    public void addToBlock(Integer instruction) {

        block.put((block.keySet().size() + 1), instruction);
    }

    /*
     * Removes the given instruction from the basicBlock if it already exists.
     * Returns true on successful removal and false otherwise
     */
    public boolean removeFromBlock(Integer instructionNumber) {
        return block.remove(instructionNumber) != null;
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

    /*
     *
     */
    public boolean removeDominatingBlock(BasicBlock block) {
        return dominators.remove(block);
    }

}
