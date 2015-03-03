package com.acc.data;

import com.acc.structure.BasicBlock;

/**
 * Created by Rumpy on 18-02-2015.
 */
public class InstructionCompartment {
    private Instruction instruction;
    private BasicBlock basicBlock;

    public InstructionCompartment(Instruction instruction, BasicBlock basicBlock)
    {
        this.instruction=instruction;
        this.basicBlock=basicBlock;
    }


    public Instruction getInstruction() {
        return instruction;
    }


    public BasicBlock getBasicBlock() {
        return basicBlock;
    }
}
