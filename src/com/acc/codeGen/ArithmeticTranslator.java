package com.acc.codeGen;

import com.acc.MemoryManager;
import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.exception.CodeGenerationException;

/**
 * Created by Rumpy on 03-03-2015.
 */
public class ArithmeticTranslator {

    public static void translate(MachineCode machineCode, Instruction currentInstruction, MemoryManager memoryManager) {
        Integer dlxOpCode;
        Result a = memoryManager.getInstructionRegister(currentInstruction.getLocation());
        Result b = memoryManager.getOperand(currentInstruction, currentInstruction.getX(),true);
        Result c = memoryManager.getOperand(currentInstruction,currentInstruction.getY(), false);
        if(b.isRegister() && c.isRegister())
        {
            dlxOpCode = toDlxOpCode(currentInstruction,false);  // Mapping to the equivalent non immediate dlx instructions
            AuxilaryDLXFunctions.putF2(machineCode ,dlxOpCode, a.regNo(),b.regNo(),c.regNo());
        } else if(b.isRegister() && c.isConstant())
        {
            dlxOpCode = (toDlxOpCode(currentInstruction,true));  // Mapping to the equivalent immediate dlx instructions
            AuxilaryDLXFunctions.putF1(machineCode ,dlxOpCode, a.regNo(),b.regNo(),c.value());
        }
        else if(b.isConstant() && c.isConstant())
        {
            // possible condition:
            // add #3, #4
            dlxOpCode = MachineOperationCode.ADDI;
            AuxilaryDLXFunctions.putF1(machineCode ,dlxOpCode, a.regNo(),0,b.value());
            dlxOpCode = (toDlxOpCode(currentInstruction,true));
            AuxilaryDLXFunctions.putF1(machineCode ,dlxOpCode, a.regNo(),a.regNo(),c.value());
        }
        else
            throw new CodeGenerationException("F1 instruction: \""+currentInstruction.getInstructionString()+"\" didnt have the correct format");

        memoryManager.spillCheck(a);
    }

    private static int toDlxOpCode(Instruction currentInstruction, boolean isImmediate) {
        if(isImmediate)
        {
            if(currentInstruction.getOpcode() == OperationCode.add)
                return MachineOperationCode.ADDI;
            else if(currentInstruction.getOpcode() == OperationCode.sub)
                return MachineOperationCode.SUBI;
            else if (currentInstruction.getOpcode() == OperationCode.mul)
                return MachineOperationCode.MULI;
            else if(currentInstruction.getOpcode() == OperationCode.div)
                return MachineOperationCode.DIVI;
            else
                return MachineOperationCode.CMPI;
        }
        else
        {
            if(currentInstruction.getOpcode() == OperationCode.add)
                return MachineOperationCode.ADD;
            else if(currentInstruction.getOpcode() == OperationCode.sub)
                return MachineOperationCode.SUB;
            else if (currentInstruction.getOpcode() == OperationCode.mul)
                return MachineOperationCode.MUL;
            else if(currentInstruction.getOpcode() == OperationCode.div)
                return MachineOperationCode.DIV;
            else
                return MachineOperationCode.CMP;
        }
    }


}
