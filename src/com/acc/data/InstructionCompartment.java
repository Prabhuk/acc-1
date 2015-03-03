package com.acc.data;

import com.acc.structure.BasicBlock;

/**
 * Created by Rumpy on 18-02-2015.
 */
public class InstructionCompartment {
    private Instruction instruction;
    private String dominanceFrontier;
    private BasicBlock basicBlock;

    public InstructionCompartment(Instruction instruction, String dominanceFrontier, BasicBlock basicBlock)
    {
        this.instruction=instruction;
        this.dominanceFrontier=dominanceFrontier;
        this.basicBlock=basicBlock;
    }


    public Instruction getInstruction() {
        return instruction;
    }

    public String getDominanceFrontier() {
        return dominanceFrontier;
    }


    public BasicBlock getBasicBlock() {
        return basicBlock;
    }
}
