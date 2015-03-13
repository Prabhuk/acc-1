package com.acc.codeGen;

import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.Result;

/**
 * Created by Rumpy on 11-03-2015.
 */
public class ReadWriteTranslator {

    public static void translate(Instruction currentInstruction, MachineCode machineCode, MemoryManager memoryManager) {
        Integer dlxOpCode;
        if(currentInstruction.getOpcode() == OperationCode.read)
        {
            dlxOpCode=MachineOperationCode.RDD;
            Result a = memoryManager.getInstructionRegister(currentInstruction.getLocation());
            AuxilaryDLXFunctions.putF2(machineCode, dlxOpCode, a.regNo(), 0, 0);
            memoryManager.spillCheck(a);
        }
        else if(currentInstruction.getOpcode() == OperationCode.write)
        {
            dlxOpCode=MachineOperationCode.WRD;
            Result b = memoryManager.getOperand(currentInstruction, currentInstruction.getX(), true);
            if(b.isRegister())
                AuxilaryDLXFunctions.putF2(machineCode ,dlxOpCode, 0,b.regNo(),0);
            else
            {
                AuxilaryDLXFunctions.putF1(machineCode,MachineOperationCode.ADDI, 26, 0,b.value());
                AuxilaryDLXFunctions.putF2(machineCode ,dlxOpCode, 0,26,0);
            }
        }
        else
        {
            dlxOpCode=MachineOperationCode.WRL;
            AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode,0,0,0);
        }
    }
}
