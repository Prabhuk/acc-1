package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.BasicBlock;
import com.acc.structure.ControlFlowGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * This holds the generated code
 */
public class Code {

    /*
     * List of instructions for the input program
     */
    private final List<Instruction> instructions = new ArrayList<Instruction>();
    private static ControlFlowGraph controlFlowGraph = ControlFlowGraph.getCFG();


    /**
     * @return Returns the current program counter value
     */
    public int getPc() {
        return instructions.size();
    }

    /**
     * @param instruction - Takes an instruction and appends to the output code
     * @return Returns the current program counter value
     */
    public int addCode(Instruction instruction) {
        if(instruction.isKill()) {
            final BasicBlock joinBlock = controlFlowGraph.getCurrentBlock().getJoinBlock();
            if (joinBlock != null) {
                instructions.add(instruction);
                joinBlock.addInstruction(instruction);
            }
//            else {
//                currentBlock.addInstruction(instruction);
//            }
        } else {
            instructions.add(instruction);
            if(!instruction.isPhi()) {
                controlFlowGraph.addInstruction(instruction);
            }
        }
        return instructions.size();
    }

    /**
     * @param instruction - Takes an instruction and appends to the output code
     * @return Returns the current program counter value
     */
    public int removeCode(Instruction instruction) {
        instructions.remove(instruction);
        return instructions.size();
    }

    public BasicBlock getCurrentBlock() {
        return controlFlowGraph.getCurrentBlock();
    }

    public void setCurrentBlock(BasicBlock block) {
        controlFlowGraph.setCurrentBlock(block);
    }


    public void Fixup(int location) {
//        instructions.get(location).FixUp(getPc() - location);
        instructions.get(location).FixUp(getPc());
        //$TODO$ this is not updated within the basicblock. Need to fix it in the basic block as well. Use instr number range?
    }

    public void Fixlink(Result follow) {
        //$TODO$ Needs implementation
    }

    public ControlFlowGraph getControlFlowGraph() {
        return controlFlowGraph;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

}
