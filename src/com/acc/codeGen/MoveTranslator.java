package com.acc.codeGen;

import com.acc.MemoryManager;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.util.Printer;

/**
 * Created by Rumpy on 11-03-2015.
 */
public class MoveTranslator {
    public static void translate(Instruction currentInstruction, MachineCode machineCode, MemoryManager memoryManager) {
            Result a = memoryManager.getOperand(currentInstruction,currentInstruction.getX(),true);//using isB as true since scratch reg 26 is free
            Result c = memoryManager.getOperand(currentInstruction,currentInstruction.getY(),false);
        if(a.isRegister() && c.isConstant())
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.ADDI, a.regNo(), 0, c.value());
        else
            AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.ADD, a.regNo(), 0, c.regNo());
            memoryManager.spillCheck(a);

    }
}
