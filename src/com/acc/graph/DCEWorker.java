package com.acc.graph;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.parser.Parser;
import com.acc.structure.SymbolTable;
import com.acc.util.Printer;

import java.util.*;

/**
 * Created by Rumpy on 01-03-2015.
 */

public class DCEWorker {

    private Code code;
    public DCEWorker(Code code) {
        this.code=code;
    }

    public void visit() {

        List<Instruction> allInstructions = code.getInstructions();
        ListIterator<Instruction> instructionIterator = allInstructions.listIterator();
        Map<Integer,Integer> compareResult= new HashMap<Integer, Integer>();

        while(instructionIterator.hasNext())
        {
            Instruction currentInstruction = instructionIterator.next();
            ListIterator<Instruction> instructionJumper = allInstructions.listIterator(instructionIterator.nextIndex());

            Result x = currentInstruction.getX();
            Result y = currentInstruction.getY();

            if(!currentInstruction.isDeleted()) {
                if (currentInstruction.getOpcode() == OperationCode.cmp) {
                    if (x.isConstant() && y.isConstant()) {
                        Integer result = x.value() - y.value();
                        compareResult.put(currentInstruction.getLocation(), result);
                        currentInstruction.setDeleted(true, "DCE"); //$TODO$ We do not have break statements and so assuming that while loops will not be infinite
                        Printer.debugMessage("Deleted compare instruction: " + currentInstruction.getInstructionString());
                    }
                } else if (currentInstruction.getOpcode() >= OperationCode.bne && currentInstruction.getOpcode() <= OperationCode.bgt) {
                    Integer branchCode = currentInstruction.getOpcode();
                    if (compareResult.containsKey(x.getIntermediateLoation())) {
                        Integer result = compareResult.get(x.getIntermediateLoation());
                        if (OperationCode.isConditionSatisfied(branchCode, result))  // Branch if equals
                        {
                            //eliminate the fall through
                            Integer deleteTill = currentInstruction.getY().value();
                            deleteInstructions(instructionIterator, currentInstruction, deleteTill);
                        } else {
                            //eliminate the branch code
                            deleteBranchCode(instructionJumper, currentInstruction, y);
                        }
                        currentInstruction.setDeleted(true, "DCE");
                        Printer.debugMessage("Deleted branch instr: " + currentInstruction.getInstructionString());
                    }
                }
            }
        }
    }

    private void deleteBranchCode(ListIterator<Instruction> instructionIterator, Instruction currentInstruction, Result y) {

        while(!currentInstruction.getLocation().equals(y.value()-1))
        {
            currentInstruction=instructionIterator.next();
        }
        if(currentInstruction.getOpcode().equals(OperationCode.bra)) {
            Integer deleteTill = currentInstruction.getX().value();
            if(deleteTill<currentInstruction.getLocation())
                deleteTill=currentInstruction.getLocation()+1;
            Printer.debugMessage("This is supposed to be the bra instr: " + currentInstruction.getInstructionString());
            deleteInstructions(instructionIterator, currentInstruction, deleteTill);
        }
    }

    private void deleteInstructions(ListIterator<Instruction> instructionIterator, Instruction currentInstruction, Integer deleteTill) {
        while(!currentInstruction.getLocation().equals(deleteTill))
        {
            currentInstruction.setDeleted(true,"DCE");
            Printer.debugMessage("Deleted Instr: " + currentInstruction.getLocation() + " " + currentInstruction.getInstructionString());
            currentInstruction=instructionIterator.next();
        }
    }





}
