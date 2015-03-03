package com.acc.graph;

import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.InstructionCompartment;
import com.acc.data.Result;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;
import com.acc.util.Printer;

import java.util.*;

/**
 * Created by Rumpy on 17-02-2015.
 */
public class CSEWorker extends Worker {

    private Map<Integer, List<InstructionCompartment>> index = new HashMap<Integer, List<InstructionCompartment>>();
    private Map<Integer, Integer> deletedLocationRedirectionTable = new HashMap<Integer, Integer>();

    public CSEWorker(Parser parser) {
        super(parser);
    }

    @Override
    public void visit(BasicBlock node) {

        List<InstructionCompartment> anchorObject;
        for (Instruction currentInstruction : node.getInstructions()) //go through all the instructions on the BB
        {
            //preparing current instruction with the changed locations of prior deletions
            //Dont reform them if they are branch statements
            Integer opcode = currentInstruction.getOpcode();

            final Result xCurrent = currentInstruction.getX();
            final Result yCurrent = currentInstruction.getY();

            if ((opcode >= OperationCode.add && opcode <= OperationCode.phi) ||
                    (opcode == OperationCode.kill))//Checking for arithmetic operations along with phi and kill
            {
                if (xCurrent != null) {
                    reformVariable(xCurrent);
                }
                if (yCurrent != null) {
                    reformVariable(yCurrent);
                }

                String currentInstructionString = currentInstruction.getInstructionString();
                InstructionCompartment instructionCompartment = new InstructionCompartment(currentInstruction, node);
                if (index.containsKey(opcode)) {
                    //Add opcode to an existing linked list
                    //Printer.debugMessage(((Integer)index).toString()+" ---- "+currentInstructionString);
                    anchorObject = index.get(opcode);
                    //Since we are adding to an existing linked list, we now check for CSE possibilities in the list
                    boolean cse = false;
                    InstructionCompartment instructionList = null;
                    for (InstructionCompartment i : anchorObject) {
                        instructionList = i;
                        String instructionListString = instructionList.getInstruction().getInstructionString();

                        if (instructionListString.equals(currentInstructionString) && (instructionList.getBasicBlock().isDominatingOver(node) || instructionList.getBasicBlock().equals(node))
                                && !currentInstruction.isPhi() && !currentInstruction.isKill()) {
                            //now that they are identical instructions, im going to check if their values haven't changed.
                            // set: cse = true; : when i know its a common sub expression elim case.
                            //It'll be a CSE elim case if 1. They both have constants as X and Y; 2. They have variables that dont change
                            //checking for constants
                            if (currentInstruction.getOpcode().equals(OperationCode.load) || currentInstruction.getOpcode().equals(OperationCode.store)) {
                                //Checking for load or store operations first since they are single operand instructions
                                if (!isVariableKilledBetween(xCurrent, node, instructionList)) {
                                    cse = true;
                                    break;
                                }
                            }

                            if (!xCurrent.isVariable() && yCurrent.isVariable()) {
                                if (!isVariableKilledBetween(yCurrent, node, instructionList)) {
                                    cse = true;
                                    //IS A Common sub expression
                                    break;
                                }

                            } else if (xCurrent.isVariable() && !yCurrent.isVariable()) {
                                if (!isVariableKilledBetween(xCurrent, node, instructionList)) {
                                    cse = true;
                                    //IS A Common sub expression
                                    break;
                                }
                            } else if (xCurrent.isVariable() && yCurrent.isVariable()) {
                                if (!isVariableKilledBetween(xCurrent, node, instructionList) && !isVariableKilledBetween(yCurrent, node, instructionList)) {
                                    cse = true;
                                    //IS A Common sub expression
                                    break;
                                }
                            } else {
                                cse = true;
                                //IS A Common sub expression
                                break;
                            }
                            break;
                        }
                    }
                    if (!cse) {
                        //Finally adding this guy
                        anchorObject.add(instructionCompartment);
                        index.put(opcode, anchorObject);


                    } else {
                        //DEAL WITH THE REMOVAL OF THE CURRENT INSTRUCTION
                        Printer.debugMessage("WE HAVE FOUND A COMMON SUB EXPRESSION SITUATION");
                        Printer.debugMessage(currentInstruction.getLocation().toString() + " " + currentInstructionString);
                        deletedLocationRedirectionTable.put(currentInstruction.getLocation(), instructionList.getInstruction().getLocation());
                        currentInstruction.setDeleted(true, "CSE");
                    }
                } else {
                    //Put the op code in the index index
                    //Printer.debugMessage(((Integer) index).toString()+" ---- "+currentInstructionString);


                    //prep the element in a compartment
                    //Create a new op linked list
                    anchorObject = new ArrayList<InstructionCompartment>();
                    anchorObject.add(instructionCompartment);
                    index.put(opcode, anchorObject);

                }
            }
        }

    }

    private void reformVariable(Result z) {
        if (z.isIntermediate()) {
            Integer intermediateLoc = z.getIntermediateLoation();
            if (deletedLocationRedirectionTable.containsKey(intermediateLoc)) {
                z.setIntermediateLoation(deletedLocationRedirectionTable.get(intermediateLoc));
            }
        }
    }

    private boolean isVariableKilledBetween(Result variable, BasicBlock node, InstructionCompartment priorCompartment) {

        List<InstructionCompartment> killAnchorObject;
        if (variable.isArray())
            killAnchorObject = index.get(OperationCode.kill);
        else
            killAnchorObject = index.get(OperationCode.phi);
        if (killAnchorObject == null) {
            return false;
        }
        ListIterator<InstructionCompartment> InstructionIterator = killAnchorObject.listIterator();
        InstructionCompartment holder;
        while (InstructionIterator.hasNext()) {
            holder = InstructionIterator.next();
            Printer.debugMessage("[139]" + holder.getInstruction().getInstructionString());
            Printer.debugMessage("a." + variable.getVariableName());
            final Result x = holder.getInstruction().getX();
            Printer.debugMessage("b." + holder.getInstruction().getSymbol().getName());
            if (x.isVariable()) {
                if (x.getVariableName().equals(variable.getVariableName())) {
                    if (holder.getBasicBlock().isDominatingOver(node) && priorCompartment.getInstruction().getLocation() > holder.getInstruction().getLocation()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
