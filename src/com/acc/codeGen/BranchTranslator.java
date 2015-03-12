package com.acc.codeGen;

import com.acc.MemoryManager;
import com.acc.constants.OperationCode;
import com.acc.data.BranchFixupTable;
import com.acc.data.Instruction;
import com.acc.data.Result;


import java.util.Map;

/**
 * Created by Rumpy on 11-03-2015.
 */
public class BranchTranslator {

   public static void translate(Instruction currentInstruction , Map instructionMap, BranchFixupTable branchFixupTable, MemoryManager memoryManager, MachineCode machineCode) {

        if(currentInstruction.getOpcode() == OperationCode.bra)
        {
            Integer dlxOpCode;
            Result c = currentInstruction.getX();
            dlxOpCode= MachineOperationCode.BEQ;

            if(instructionMap.containsKey(c.value()))
            {
                AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, 0,0,(Integer)instructionMap.get(c.value()));
            }
            else
            {
                MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, 0, 0, -1);
                branchFixupTable.add(c.value(),machineInstruction);
            }
        }
        else {
            Integer dlxOpCode;
            Result c = currentInstruction.getY();
            Result a = memoryManager.getInstructionRegister(currentInstruction.getX().getIntermediateLoation());
            if (currentInstruction.getOpcode() == OperationCode.bne) {
                dlxOpCode= MachineOperationCode.BNE;
                MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, a.regNo(), 0, -1);
                branchFixupTable.add(c.value(),machineInstruction);
            } else if (currentInstruction.getOpcode() == OperationCode.beq) {
                dlxOpCode=MachineOperationCode. BEQ;
                MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, a.regNo(), 0, -1);
                branchFixupTable.add(c.value(),machineInstruction);
            } else if (currentInstruction.getOpcode() == OperationCode.ble) {
                dlxOpCode= MachineOperationCode.BLE;
                MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, a.regNo(), 0, -1);
                branchFixupTable.add(c.value(),machineInstruction);
            } else if (currentInstruction.getOpcode() == OperationCode.blt) {
                dlxOpCode= MachineOperationCode.BLT;
                MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, a.regNo(), 0, -1);
                branchFixupTable.add(c.value(),machineInstruction);
            } else if (currentInstruction.getOpcode() == OperationCode.bgt) {
                dlxOpCode= MachineOperationCode.BGT;
                MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, a.regNo(), 0, -1);
                branchFixupTable.add(c.value(),machineInstruction);
            } else {
                //bge
                dlxOpCode= MachineOperationCode.BGE;
                MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, a.regNo(), 0, -1);
                branchFixupTable.add(c.value(),machineInstruction);
            }

        }
    }

}
