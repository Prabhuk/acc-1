package com.acc.graph;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.structure.SymbolTable;
import com.acc.util.Printer;

import java.util.*;

/**
 * Created by Rumpy on 01-03-2015.
 */

public class DCEWorker {

    SymbolTable symbolTable;
    Code code;
    public DCEWorker(SymbolTable symbolTable, Code code) {
        this.symbolTable=symbolTable;
        this.code=code;
    }

    public void visit() {

        List<Instruction> allInstructions = code.getInstructions();
        ListIterator<Instruction> instructionIterator = allInstructions.listIterator();
        Map<Integer,Integer> compareResult= new HashMap<Integer, Integer>();

        while(instructionIterator.hasNext())
        {
            ListIterator<Instruction> instructionJumper = allInstructions.listIterator(instructionIterator.nextIndex());
            Instruction currentInstruction = instructionIterator.next();
            if(!currentInstruction.isDeleted()) {
                if (currentInstruction.getOpcode() == OperationCode.cmp) {
                    Instruction compareInstruction = currentInstruction;
                    Result x = compareInstruction.getX();
                    Result y = compareInstruction.getY();
                    if (isConstant(x) && isConstant(y)) {
                        Integer result = x.value() - y.value();
                        compareResult.put(compareInstruction.getLocation(), result);
                        compareInstruction.setDeleted(true, "DCE");
                        Printer.debugMessage("Deleted compare instruction: " + compareInstruction.getInstructionString());
                    }

                } else if (currentInstruction.getOpcode() >= OperationCode.bne && currentInstruction.getOpcode() <= OperationCode.bgt) {
                    Instruction branchInstruction = currentInstruction;
                    Integer branchCode = branchInstruction.getOpcode();

                    Result x = branchInstruction.getX();
                    Result y = branchInstruction.getY();
                    if (compareResult.containsKey(x.getIntermediateLoation())) {
                        Integer result = compareResult.get(x.getIntermediateLoation());
                        if (branchCode.equals(OperationCode.beq))  // Branch if equals
                        {
                            if (result == 0) {
                                //eliminate the fall through
                                Integer deleteTill = branchInstruction.getY().value();
                                deleteInstructions(instructionIterator, currentInstruction, deleteTill);
                            } else {
                                //eliminate the branch code
                                deleteBranchCode(instructionJumper, currentInstruction, y);
                            }
                        } else if (branchCode.equals(OperationCode.bne))  //Branch if not equals
                        {
                            if (result != 0) {
                                //eliminate the fall through
                                Integer deleteTill = branchInstruction.getY().value();
                                deleteInstructions(instructionIterator, currentInstruction, deleteTill);
                            } else {
                                //eliminate the branch code
                                deleteBranchCode(instructionJumper, currentInstruction, y);
                            }
                        } else if (branchCode.equals(OperationCode.ble))  //Branch if less than equals
                        {
                            if (result <= 0) {
                                //eliminate the fall through
                                Integer deleteTill = branchInstruction.getY().value();
                                deleteInstructions(instructionIterator, currentInstruction, deleteTill);
                            } else {
                                //eliminate the branch code
                                deleteBranchCode(instructionJumper, currentInstruction, y);
                            }
                        } else if (branchCode.equals(OperationCode.blt))  //Branch if less than
                        {
                            if (result < 0) {
                                //eliminate the fall through
                                Integer deleteTill = branchInstruction.getY().value();
                                deleteInstructions(instructionIterator, currentInstruction, deleteTill);
                            } else {
                                //eliminate the branch code
                                deleteBranchCode(instructionJumper, currentInstruction, y);
                            }
                        } else if (branchCode.equals(OperationCode.bge))  //Branch if not equals
                        {
                            if (result >= 0) {
                                //eliminate the fall through
                                Integer deleteTill = branchInstruction.getY().value();
                                deleteInstructions(instructionIterator, currentInstruction, deleteTill);
                            } else {
                                //eliminate the branch code
                                deleteBranchCode(instructionJumper, currentInstruction, y);
                            }
                        } else if (branchCode.equals(OperationCode.bgt)) {
                            if (result > 0) {
                                //eliminate the fall through
                                Integer deleteTill = branchInstruction.getY().value();
                                deleteInstructions(instructionIterator, currentInstruction, deleteTill);
                            } else {
                                //eliminate the branch block
                                deleteBranchCode(instructionJumper, currentInstruction, y);
                            }
                        }
                        branchInstruction.setDeleted(true, "DCE");
                        Printer.debugMessage("Deleted branch instr: " + branchInstruction.getInstructionString());
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


    public boolean isConstant(Result z)
    {
        if(z.kind().isConstant())
            return true;
        return false;
    }



}
