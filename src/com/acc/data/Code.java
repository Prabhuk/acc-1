package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.BasicBlock;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * This holds the generated code
 */
public class Code {

    /*
     * List of instructions for the input program
     */
    private final List<Instruction> instructions = new LinkedList<Instruction>();
    private final List<BasicBlock> basicBlocks = new LinkedList<BasicBlock>();
    private BasicBlock currentBlock;

    public Code() {
        currentBlock = new BasicBlock();
        basicBlocks.add(currentBlock);
    }

    /**
     * @return Returns the current program counter value
     */
    public int getPc() {
        return instructions.size();
    }

    /**
     * @param instruction - Takes an instruction and appends to the output code
     * @param instructionCode
     * @return Returns the current program counter value
     */
    public int addCode(Instruction instruction, int instructionCode) {
        instructions.add(instruction);
        currentBlock.addToBlock(instruction);
        if (instructionCode >= OperationCode.BEQ && instructionCode <= OperationCode.RET) {
            addBasicBlock();
        }
        return instructions.size();
    }

    /*
     * Creates a new Basic Block to which instructions will be added in the future
     */

    private void addBasicBlock() {
        final BasicBlock temp = new BasicBlock();
        currentBlock.addDominatedOverBlock(temp);
        currentBlock = temp;
        basicBlocks.add(currentBlock);

    }

    public void Fixup(int location) {
        instructions.get(location).FixUp(getPc() - location);
        //$TODO$ this is not updated within the basicblock. Need to fix it in the basic block as well. Use instr number range?
    }

    public void Fixlink(Result follow) {

    }

    /*
     * @return Returns the current Basic Block
     */
    public BasicBlock getCurrentBlock() {
        return currentBlock;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}
