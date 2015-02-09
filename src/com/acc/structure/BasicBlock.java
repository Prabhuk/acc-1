package com.acc.structure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by prabhuk on 1/25/2015.
 * Datastructure to store the instructions in a single basicBlock
 */
public class BasicBlock {
    /*
     * Storing the instruction number along with the
     */
    private final Map<Integer, Integer> block = new LinkedHashMap<Integer, Integer>();
    private final Set<BasicBlock> dominatesOver = new HashSet<BasicBlock>();

    public Set<BasicBlock> getDominatesOver() {
        return dominatesOver;
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
    public boolean isDominatingOver(BasicBlock block) {
        return dominatesOver.contains(block);
    }

    /*
     * The block parameter will be added as a dominating block for the current block
     */
    public void addDominatedOverBlock(BasicBlock block) {
        dominatesOver.add(block);
    }

    /*
     *
     */
    public boolean removeDominatingOverBlock(BasicBlock block) {
        return dominatesOver.remove(block);
    }

}
