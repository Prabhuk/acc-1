package com.acc.codeGen;

import com.acc.data.Result;
import com.acc.util.Printer;

/**
 * Created by Rumpy on 03-03-2015.
 */
public class AuxilaryDLXFunctions {

    public static MachineInstruction putF1(MachineCode code, int instructionCode, int a, int b, int c) {

      if (c < 0)
          c ^= 0xFFFF0000;
        if ((a & ~0x1F | b & ~0x1F | c & ~0xFFFF) != 0)
            System.out.println("Illegal Operand(s) for F1 Format.");
        final int ins = instructionCode << 26 | a << 21 | b << 16 | c;
        final MachineInstruction instruction = new MachineInstruction(instructionCode, a, b, c, code.getPc(),ins);

        Printer.debugMessage(Integer.toString(code.getPc())+" : "+MachineOperationCode.getOperationName(instructionCode)+" "+Integer.toString(a)+" "+Integer.toString(b)+" "+Integer.toString(c));
        code.addCode(instruction);
        return instruction;
    }

    public static MachineInstruction putF2(MachineCode code, int instructionCode, int a, int b, int c) {

        if ((a & ~0x1F | b & ~0x1F | c & ~0x1F) != 0)
            System.out.println("Illegal Operand(s) for F2 Format.");
        final int ins = instructionCode << 26 | a << 21 | b << 16  | c;
        final MachineInstruction instruction = new MachineInstruction(instructionCode, a, b, c, code.getPc(),ins);
        Printer.debugMessage(Integer.toString(code.getPc())+" : "+MachineOperationCode.getOperationName(instructionCode)+" "+Integer.toString(a)+" "+Integer.toString(b)+" "+Integer.toString(c));
        code.addCode(instruction);
        return instruction;
    }
//
//    public static void putF3(Code code, int instructionCode, int c) {
//        final int ins = instructionCode << 26 | c;
//        final Instruction instruction = new Instruction(ins, instructionCode, null, null, c, null, null);
//        code.addCode(instruction);
//    }
}
