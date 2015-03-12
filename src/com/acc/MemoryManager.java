package com.acc;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rumpy on 05-03-2015.
 */
public class MemoryManager {
    private Map<Integer,Integer> registerLocations;
    private Map<Integer,Integer> offsetLocations;
    private MachineCode machineCode;
    private int offsetcoutner;
    public MemoryManager(Computation program, MachineCode machineCode)
    {
        registerLocations = new HashMap<Integer, Integer>();
        offsetLocations = new HashMap<Integer, Integer>();
        this.machineCode=machineCode;
        int registercount=1;
        offsetcoutner = 0;
        Map<Integer,Integer> regInfo = program.getRegisterInfo();
        for(Integer i:regInfo.keySet())
        {
            if(regInfo.get(i)<8)
            {
                registerLocations.put(i,registercount);
                registercount++;
            }
            else
            {
                offsetLocations.put(i,offsetcoutner);
                offsetcoutner++;
                AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 0,29,-4);
            }
        }
    }

    public Result getInstructionRegister(Integer location) {
        //For the location on the intermediate address list please return a result with kind set to reg and regno set to the register number
        if(registerLocations.containsKey(location))
        {
            return new Result(Kind.REG, registerLocations.get(location),null, null, null,null, null);
        }
        int offset = offsetcoutner - offsetLocations.get(location);
        return new Result(Kind.REG, 25, offset,null, null,null, null);
        //fixup register 25 in memory after
    }

    public Result getOperand(Instruction currentInstruction, Result x, boolean isB) {
        //This guy has to return a new Result that sets the kind to Register or Constant for the given result
        if(x.isConstant()) {
            return new Result(Kind.CONSTANT, null, x.value(), null, null, null, null);
        }
        else if(x.isRegister())
        {

            return x;
//            Integer regInfo = x.regNo();
//            if(regInfo<8)
//            {
//                return new Result(Kind.REG, regInfo+1,null,null,null,null);
//            }
//            else
//            {
//                int offset = offsetcoutner - offsetLocations.get(x.getIntermediateLoation());
//
//            }

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
