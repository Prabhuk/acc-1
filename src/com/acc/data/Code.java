package com.acc.data;

import com.acc.structure.BasicBlock;
import com.acc.structure.ControlFlowGraph;
import com.acc.structure.SymbolTable;

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
    private final ControlFlowGraph controlFlowGraph;
    private String programName;

    public Code() {
        this.controlFlowGraph = new ControlFlowGraph();
    }

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
        return addCode(instruction, -1);
    }

    public int addCode(Instruction instruction, int targetIndex) {
        if(instruction.isKill()) {
            final BasicBlock joinBlock = controlFlowGraph.getCurrentBlock().getJoinBlock();
            if (joinBlock != null) {
                if(targetIndex != -1) {
                    instructions.add(targetIndex, instruction);
                } else {
                    instructions.add(instruction);
                }
                joinBlock.addInstruction(instruction);
            }
        } else {
            if(targetIndex != -1) {
                instructions.add(targetIndex, instruction);
            } else {
                instructions.add(instruction);
            }
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
        for (Instruction instruction : instructions) {
            if(instruction.getLocation() == location) {
                instruction.FixUp(getPc());
                break;
            }
        }
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

    public int addCode(Instruction phi, Instruction instruction) {
        return addCode(phi, instructions.indexOf(instruction));
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
