package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.constants.SSAOpCodes;
import com.acc.structure.BasicBlock;
import com.acc.structure.ControlFlowGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * This holds the generated code
 */
public class SSACode {

    /*
     * List of instructions for the input program
     */
    private final List<SSAInstruction> instructions = new ArrayList<SSAInstruction>();
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
    public int addCode(SSAInstruction instruction) {
        instructions.add(instruction);
        controlFlowGraph.addSSAInstruction(instruction);
        //$TODO$ add kill if needed
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

    public List<SSAInstruction> getInstructions() {
        return instructions;
    }

    public static ControlFlowGraph getControlFlowGraph() {
        return controlFlowGraph;
    }
}
