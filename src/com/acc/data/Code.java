package com.acc.data;

import com.acc.constants.OperationCode;
import com.acc.structure.BasicBlock;
import com.acc.structure.ControlFlowGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Integer> functionCodeLocations = new HashMap<String, Integer>();

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
        if(getCurrentBlock().getInstructions().isEmpty()) {
            final int location = instruction.getLocation();
            final Instruction noop = new Instruction(OperationCode.noop, null, null, location);
            instructions.add(noop);
            controlFlowGraph.addInstruction(noop);
            instruction.setLocation(getPc());
            if(instruction.getOpcode() == OperationCode.move || instruction.isPhi()) {
                if(instruction.getX().isVariable()) {
                    if(instruction.getX().getLocation() == location) {
                        instruction.getX().setLocation(getPc());
                        instruction.getSymbol().setSuffix(getPc());
                    }
                }
                if(instruction.isPhi()) {
                    if(instruction.getY().isVariable()) {
                        if(instruction.getY().getLocation() == location) {
                            instruction.getY().setLocation(getPc());
                            instruction.getSymbol().setSuffix(getPc());
                        }
                    }
                }
            }
        }
        if(instruction.isKill()) {
            final BasicBlock joinBlock = controlFlowGraph.getCurrentBlock().getJoinBlock();
            if (joinBlock != null) {
                if(targetIndex != -1) {
                    instructions.add(targetIndex, instruction);
                } else {
                    instructions.add(instruction);
                }
                joinBlock.addInstruction(instruction);
            } else {
                if(targetIndex != -1) {
                    instructions.add(targetIndex, instruction);
                } else {
                    instructions.add(instruction);
                }
                controlFlowGraph.addInstruction(instruction);
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

    public void setLastNode() {
        controlFlowGraph.setLastNode();
    }

    public void setCurrentBlock(BasicBlock block) {
        controlFlowGraph.setCurrentBlock(block);
    }


    public void Fixup(int location) {
//        instructions.get(location).FixUp(getPc() - location);
        for (Instruction instruction : instructions) {
            if(instruction.getLocation() == location) {
                instruction.fixup(getPc());
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

    public Map<String, Integer> getFunctionCodeLocations() {
        return functionCodeLocations;
    }

    public void setFunctionCodeLocations(Map<String, Integer> functionCodeLocations) {
        this.functionCodeLocations = functionCodeLocations;
    }
}
