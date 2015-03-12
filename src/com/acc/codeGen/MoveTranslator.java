package com.acc.codeGen;

import com.acc.MemoryManager;
import com.acc.data.Instruction;
import com.acc.util.Printer;

/**
 * Created by Rumpy on 11-03-2015.
 */
public class MoveTranslator {
    public static void translate(Instruction currentInstruction, MachineCode machineCode, MemoryManager memoryManager) {
        if(currentInstruction.getX().isRegister())
        {
            Printer.debugMessage("X IS A REG"+Integer.toString((currentInstruction.getX().regNo())));
        }
        if(currentInstruction.getY().isConstant())
        {
            Printer.debugMessage("Y IS A REG");
        }

    }
}
