package com.acc.codeGen;

import com.acc.data.Instruction;
import com.acc.structure.BasicBlock;
import com.acc.util.Printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rumpy on 04-03-2015.
 */
public class MachineCode {
    private final List<MachineInstruction> instructions = new ArrayList<MachineInstruction>();

    private final List<Integer> instructionList = new ArrayList<Integer>();

    public List<MachineInstruction> getInstructions() {
        return instructions;
    }
    public int getPc() {
        return instructions.size();
    }

    public boolean addCode(MachineInstruction instruction) {
        return instructions.add(instruction);
    }

    public boolean addCode(int instruction) {
        return instructionList.add(instruction);
    }

    public int removeCode(Instruction instruction) {
        instructions.remove(instruction);
        return instructions.size();
    }
    public int[] getInstructionList() {
        int DLXInstructions[] = new int[instructions.size()];
        int i=0;

        for(MachineInstruction mi : instructions)
        {
            DLXInstructions[mi.location]=mi.getMachineCode();
        }
        return DLXInstructions;
    }
    public void Fixup(int location) {
//        instructions.get(location).FixUp(getPc() - location);
        for (MachineInstruction instruction : instructions) {
            if(instruction.getLocation() == location) {
                instruction.fixup(getPc());
                break;
            }
        }
    }






}
