package com.acc.codeGen;

import com.acc.parser.Computation;

/**
 * Created by Rumpy on 13-03-2015.
 */
public class EndTranslator {

    public static void translate(MachineCode machineCode, Computation program, int parameterSize) {
        if(program.getProgramName().equals("main"))
        {
            AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.RET, 0, 0, 0);
        }
        else
        {
            //EPILOGUE
            //Setting sp = fp
            AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.ADD, 29, 0, 28);
            //making fp = old fp
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 28, 29, 4);
            //popping the return address into R31
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 31, 29, 4);
            //popping parameters
            //todo insert code here
            for(int i=0; i<parameterSize; i++)
            {
                AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 0, 29, 4);
            }
            //popping registers
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 8, 29, 4);
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 7, 29, 4);
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 6, 29, 4);
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 5, 29, 4);
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 4, 29, 4);
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 3, 29, 4);
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 2, 29, 4);
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.POP, 1, 29, 4);
            //Returning to R31
            AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.RET, 0, 0, 31);

        }
    }
}
