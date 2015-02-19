package com.acc.structure;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.PhiInstruction;

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
    private final Map<String, Instruction> phiMap = new HashMap<String, Instruction>();
    private final Set<BasicBlock> dominatesOver = new HashSet<BasicBlock>();
    private final Set<BasicBlock> children = new HashSet<BasicBlock>();
    private final Set<BasicBlock> parents = new HashSet<BasicBlock>();
    private boolean isVisited = false;

    private static volatile int count = 0;
    private int label = 0;
    private BasicBlock joinBlock;
    private BasicBlock left;
    private BasicBlock right;

    public BasicBlock() {
        label = count++;
    }

    public Set<BasicBlock> getDominatesOver() {
        return dominatesOver;
    }

    /*
     * Adds an instruction to the basicBlock
     */
    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    /**
     * Adds the phi instruction to this basic block.
     * Caller of this method is responsible for adding it to Code in the right place
     * @param instruction
     */
    public void addPhiInstruction(Instruction instruction) {
        int index = 0;
        for (Instruction instruction1 : instructions) {
            if(instruction.getOpcode() != OperationCode.PHI) {
                break;
            }
            index++;
        }
        instructions.add(index, instruction); //PHI should always be added to the top
        phiMap.put(instruction.getSymbol().getName(), instruction);
    }

    public Instruction getPhiInstruction(String symbolName) {
        return phiMap.get(symbolName);
    }
    public Instruction removePhiInstruction(String symbolName) {
        return phiMap.remove(symbolName);
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

    public Collection<Instruction> getAllPhiInstructions() {
        return phiMap.values();
    }

    public BasicBlock getLeft() {
        return left;
    }

    public void setLeft(BasicBlock left) {
        this.left = left;
    }

    public BasicBlock getRight() {
        return right;
    }

    public void setRight(BasicBlock right) {
        this.right = right;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setJoinBlock(BasicBlock joinBlock) {
        this.joinBlock = joinBlock;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

}
