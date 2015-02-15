package com.acc.structure;

import com.acc.data.Instruction;

import java.util.*;

/**
 * Created by prabhuk on 1/25/2015.
 * Datastructure to store the instructions in a single basicBlock
 */
public class BasicBlock {
    /*
     * Storing the instruction number along with the instruction
     */
    private final List<Instruction> instructions = new ArrayList<Instruction>();
    private final Set<BasicBlock> dominatesOver = new HashSet<BasicBlock>();
    private final Set<BasicBlock> children = new HashSet<BasicBlock>();
    private final Set<BasicBlock> parents = new HashSet<BasicBlock>();
    private BasicBlock joinBlock;

    public Set<BasicBlock> getDominatesOver() {
        return dominatesOver;
    }
    /*
     * Adds an instruction to the basicBlock
     */
    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
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

    public Set<BasicBlock> getChildren() {
        return children;
    }

    public Set<BasicBlock> getParents() {
        return parents;
    }

    public void addChild(BasicBlock child) {
        child.getParents().add(this);
        this.children.add(child);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public BasicBlock getJoinBlock() {
        if(joinBlock == null) {
            joinBlock = new BasicBlock();
        }
        return joinBlock;
    }

    public void commitLoopPhis(BasicBlock block) {

        final List<Instruction> instructions = joinBlock.getInstructions();
        for (int i = instructions.size() - 1; i >= 0; i--) {
            block.getInstructions().add(0, instructions.get(i));
        }
        block.updateParentPhis();
        //$TODO$ figure out who calls process Join. (Fixup, bj etc.) Distinguish if and while here

    }

    private void updateParentPhis() {
        //$TODO$ update Phis to the outer loop
    }

    public void commitIFELSEPhis() {
        //$TODO$ figure out who calls process Join. (Fixup, bj etc.) Distinguish if and while here
        joinBlock.updateParentPhis();
    }

}
