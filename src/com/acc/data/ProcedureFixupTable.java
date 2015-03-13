package com.acc.data;

import com.acc.codeGen.MachineInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rumpy on 03-03-2015.
 */
public class ProcedureFixupTable {
    Map<String,List<MachineInstruction>> procedureMap;

    public ProcedureFixupTable()
    {
        procedureMap = new HashMap<String, List<MachineInstruction>>();
    }

    public void add(String procedureName, MachineInstruction instruction)
    {
        if(procedureMap.containsKey(procedureName))
        {
            List<MachineInstruction> fixupInstructions = procedureMap.get(procedureName);
            fixupInstructions.add(instruction);
        }
        else {
            List<MachineInstruction> fixupInstructions = new ArrayList<MachineInstruction>();
            fixupInstructions.add(instruction);
            procedureMap.put(procedureName, fixupInstructions);
        }
    }

    public List<MachineInstruction> getFixupInstructions(String procedureName)
    {
        if(procedureMap.containsKey(procedureName))
            return procedureMap.get(procedureName);
        return null;
    }

    public void fix(String procedureName, Integer address)
    {
        List<MachineInstruction> fixupInstructions = procedureMap.get(procedureName);
        for(MachineInstruction instruction: fixupInstructions)
        {
            instruction.fixup(address - instruction.getLocation());
        }
    }
}
