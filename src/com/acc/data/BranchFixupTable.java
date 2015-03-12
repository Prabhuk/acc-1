package com.acc.data;

import com.acc.codeGen.MachineInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rumpy on 06-03-2015.
 */
public class BranchFixupTable {
    Map<Integer,List<MachineInstruction>> branchMap;
    public BranchFixupTable()
    {
        branchMap =  new HashMap<Integer, List<MachineInstruction>>();
    }

    public void add(Integer branchTo, MachineInstruction instruction)
    {
        if(branchMap.containsKey(branchTo))
        {
            List<MachineInstruction> fixupInstructions = branchMap.get(branchTo);
            fixupInstructions.add(instruction);
        }
        else {
            List<MachineInstruction> fixupInstructions = new ArrayList<MachineInstruction>();
            fixupInstructions.add(instruction);
            branchMap.put(branchTo, fixupInstructions);
        }
    }

    public List<MachineInstruction> getFixupInstructions(Integer branchTo)
    {
        if(branchMap.containsKey(branchTo))
            return branchMap.get(branchTo);
        return null;
    }

    public void fix(Integer branchTo /*SSA address*/, Integer fixUpPoint /*Actual Machine Address*/)
    {
        if(branchMap.containsKey(branchTo)) {
            List<MachineInstruction> fixupInstructions = branchMap.get(branchTo);
            for (MachineInstruction instruction : fixupInstructions) {
                instruction.fixup(fixUpPoint);
            }
        }
    }
}


