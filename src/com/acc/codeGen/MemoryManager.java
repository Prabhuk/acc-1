package com.acc.codeGen;

import com.acc.codeGen.AuxilaryDLXFunctions;
import com.acc.codeGen.MachineCode;
import com.acc.codeGen.MachineOperationCode;
import com.acc.constants.Condition;
import com.acc.constants.Kind;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.exception.RegisterAllocationException;
import com.acc.parser.Computation;
import com.acc.structure.Register;
import com.acc.structure.Symbol;
import com.acc.util.Printer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rumpy on 05-03-2015.
 */
public class MemoryManager {
    private Map<Integer,Integer> registerLocations;
    private Map<Integer,Integer> offsetLocations;
    private MachineCode machineCode;
    private int offsetcoutner;
    Map<String,Integer> variableOffsets;

    public MemoryManager(Computation program, MachineCode machineCode)
    {
        registerLocations = new HashMap<Integer, Integer>();
        offsetLocations = new HashMap<Integer, Integer>();
        this.machineCode=machineCode;
        int registercount=1;
        offsetcoutner = 0;
        variableOffsets = new HashMap<String, Integer>();
        int paramref = 0;
        if(!program.getProgramName().equals("main")) {
            if (program.getArgumentNamesForProcedure(program.getProgramName()) != null || !program.getArgumentNamesForProcedure(program.getProgramName()).isEmpty()) {
                int paramsize = program.getArgumentNamesForProcedure(program.getProgramName()).size();
                for (String procNames : program.getArgumentNamesForProcedure(program.getProgramName())) {
                    variableOffsets.put(procNames, (paramsize+3) - paramref-2);
                    paramref++;
                }
            }
        }
        Map<Integer,Integer> regInfo = program.getRegisterInfo();
        for(Integer i:regInfo.keySet())
        {
            Printer.debugMessage("regdetails:   "+Integer.toString(regInfo.get(i)));
            if(regInfo.get(i)<8)
            {
                registerLocations.put(i,regInfo.get(i)+1);
                registercount++;
            }
            else
            {
                offsetLocations.put(i,(regInfo.get(i)-1)-8);
                if(((regInfo.get(i)-1)-8)>offsetcoutner) {
                    offsetcoutner = (regInfo.get(i)-1)-8;
                }
            }
        }
        if(offsetcoutner>0)
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.SUBI, 29, 0, offsetcoutner*4);
//        if(program.getProgramName().equals("main"))
//        {
//            Integer globalOffsetcounter = 0;
//            for (Symbol s : program.getSymbolTable().getSymbols()) {
//                Printer.print(s.getName());
//                GlobalMemoryTable.insertglobalvariables(s.getName(),globalOffsetcounter);
//                AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 0, 29, -4);
//                globalOffsetcounter++;
//            }
          //  AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.ADDI, 30, 29, 0);
//        }
    }

    public Result getInstructionRegister(Integer location) {
        //For the location on the intermediate address list please return a result with kind set to reg and regno set to the register number
        if(registerLocations.containsKey(location))
        {
            return new Result(Kind.REG, registerLocations.get(location),null, null, null,null, null);
        }
        else if(offsetLocations.containsKey(location)) {
            int offset = offsetcoutner - offsetLocations.get(location);
            return new Result(Kind.REG, 25, offset, null, null, null, null);
        }
        return new Result(Kind.REG, 20 , null, null, null, null);
        //todo Remove this line
    }

    public Result getOperand(Instruction currentInstruction, Result x, boolean isB) {
        //This guy has to return a new Result that sets the kind to Register or Constant for the given result
        if(x.isConstant()) {
            return new Result(Kind.CONSTANT, null, x.value(), null, null, null, null);
        }
        else if(x.isRegister())
        {
            Integer regInfo = x.regNo();
            if(regInfo<8)
            {
                return new Result(Kind.REG, regInfo+1,null,null,null,null);
            }
            else
            {
                int offset = (regInfo-1)-8;
                if(isB) {
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 26, 29, (offset * 4));
                    return new Result(Kind.REG, 26, offset, null, null, null, null);
                }
                else
                {
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 27, 29, (offset * 4));
                    return new Result(Kind.REG, 27, offset, null, null, null, null);
                }


            }

        }
        else if(x.isIntermediate())
        {
            // x is an intermediate
            if(registerLocations.containsKey(x.getIntermediateLoation()))
            {
                return new Result(Kind.REG, registerLocations.get(x.getIntermediateLoation()),null, null, null,null, null);
            }

            int offset = offsetcoutner - offsetLocations.get(x.getIntermediateLoation());
            if(isB) {
                AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 26, 29, (offset * 4));
                return new Result(Kind.REG, 26, offset, null, null, null, null);
            }
            else
            {
                AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 27, 29, (offset * 4));
                return new Result(Kind.REG, 27, offset, null, null, null, null);
            }
        }
        else if(x.isVariable())
        {
            if(isB) {
                if (variableOffsets.containsKey(x.getVariableName())) {
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 26, 28, variableOffsets.get(x.getVariableName()) * 4);
                    return new Result(Kind.REG, 26, variableOffsets.get(x.getVariableName()), null, null, null, null);
                }
                else
                    //global table code here
                    return new Result(Kind.REG, 26, variableOffsets.get(x.getVariableName()), null, null, null, null);
            }
            else
            {
                if (variableOffsets.containsKey(x.getVariableName())) {
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 27, 28, variableOffsets.get(x.getVariableName()) * 4);
                    return new Result(Kind.REG, 27, variableOffsets.get(x.getVariableName()), null, null, null, null);
                }
                else
                    //global table code here
                    return new Result(Kind.REG, 27, variableOffsets.get(x.getVariableName()), null, null, null, null);
            }

        }
        else
            return new Result(Kind.REG, 27, null,null,null,null);
    }

    public void spillCheck(Result a) {
        if(a.isRegister() && (a.regNo()>=(25) && a.regNo()<=(27) ))
        {
            int offset = a.value();
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.STW, a.regNo(), 29, (offset * 4));
        }
    }
}
