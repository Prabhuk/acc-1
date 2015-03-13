package com.acc.structure;

import com.acc.data.BlockType;
import com.acc.data.Instruction;
import com.acc.data.Result;

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
    private final List<BasicBlock> dominatesOver = new ArrayList<BasicBlock>();
    private final Set<BasicBlock> children = new HashSet<BasicBlock>();
    private Set<BasicBlock> parents = new HashSet<BasicBlock>();
    private Set<Integer> liveRanges = new HashSet<Integer>();
    private Map<String, Result> valueMap = new HashMap<String, Result>();
    private List<String> exclude = new ArrayList<String>();

    private static Set<BasicBlock> allBlocks = new HashSet<BasicBlock>();
    private boolean isVisited = false;
    private boolean isDeleted = false;

    private static volatile int count = 0;
    private int label = 0;
    private BasicBlock joinBlock;
    private BasicBlock left;
    private BasicBlock right;
    private BlockType type;

    public BasicBlock() {
        label = count++;
        allBlocks.add(this);
    }

    public static void resetCount() {
        count = 0;
    }

    public List<BasicBlock> getDominatesOver() {
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
    public Instruction addPhiInstruction(Instruction instruction) {
        int index = 0;
        Instruction nonPhi = null;
        for (Instruction instruction1 : instructions) {
            if(!instruction1.isPhi()) {
                nonPhi = instruction1;
                break;
            }
            index++;
        }
        instructions.add(index, instruction); //PHI should always be added to the top
        phiMap.put(instruction.getSymbol().getName(), instruction);
        return nonPhi;
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
        for (BasicBlock allBlock : allBlocks) {
            if(allBlock.getDominatesOver().contains(this)) {
                allBlock.getDominatesOver().add(block);
            }
        }
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

    public void setParents(Set<BasicBlock> parents) {
        this.parents = parents;
    }



    public void addChild(BasicBlock child, boolean addDominatedOver) {
        if(child.getParents().contains(this)) {
            return;
        }
        child.getParents().add(this);
        this.children.add(child);
        if(addDominatedOver) {
            this.addDominatedOverBlock(child);
        }
    }

    public void addChild(BasicBlock child) {
        addChild(child, false);
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

    public void setJoinBlock(BasicBlock joinBlock) {
        this.joinBlock = joinBlock;
    }

    public BasicBlock getJoinBlock() {
        return joinBlock;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public Set<Integer> getLiveRanges() {
        return liveRanges;
    }

    public void setLiveRanges(Set<Integer> liveRanges) {
        this.liveRanges = liveRanges;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public boolean isWhileBody() {
        return type != null && type.isWhileBody();
    }
    public boolean isWhileHead() {
        return type != null && type.isWhileHead();
    }
    public boolean isCompare() {
        return type != null && type.isCompare();
    }

    public boolean isIF() {
        return type != null && type.isIF();
    }

    public boolean isWhileFollow() {
        return type != null && type.isWhileFollow();
    }
    public boolean isCall() {
        return type != null && type.isCall();
    }

    @Override
    public String toString() {
        return String.valueOf(label);
    }

    public static void setAllBlocks(Set<BasicBlock> allBlocks) {
        BasicBlock.allBlocks = allBlocks;
    }

    public void updateValueMap(Map<String, Result> valueMap) {
        for (String s : valueMap.keySet()) {
            this.valueMap.put(s, valueMap.get(s));
        }
    }

    public void updateExclude(List<String> exclude) {
        for (String s : exclude) {
            this.exclude.addAll(exclude);
        }
    }



    public Map<String, Result> getValueMap() {
        return valueMap;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void addToExclude(String variableName) {
        this.exclude.add(variableName);
    }

    public void removeFromExclude(String variableName) {
        this.exclude.remove(variableName);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
