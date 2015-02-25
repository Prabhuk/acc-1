package com.acc.graph;

import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.util.Printer;

import java.util.*;

/**
 * Created by prabhuk on 2/23/2015.
 */
public class CopyPropogationWorker extends Worker {

    private Map<String, Result> valueMap = new HashMap<String, Result>();
//    private List<Instruction> copiedInstructions = new ArrayList<Instruction>();

    public CopyPropogationWorker(SymbolTable symbolTable) {
        super(symbolTable);
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void visit(BasicBlock node) {

        final List<Instruction> instructions = node.getInstructions();
        final Set<BasicBlock> dominatesOver = node.getDominatesOver();
        processInstructions(instructions);
        for (BasicBlock basicBlock : dominatesOver) {
            updateDominatedBy(basicBlock.getInstructions());
        }
        valueMap = new HashMap<String, Result>();
    }

    private void processInstructions(List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            final Integer opcode = instruction.getOpcode();
            if(instruction.isPhi()) {
                valueMap.remove(instruction.getSymbol().getName());
                continue;
            }
            final String variableName = instruction.getX().getVariableName();
            if(instruction.isKill()) {
                valueMap.remove(variableName);
                continue;
            } else {
                final Result x = instruction.getX();
                final Result y = instruction.getY();
                if(opcode == OperationCode.move) {
                    updateValueMap(instruction, variableName, y);
                } else {
                    if (x != null) {
                        final Result result = valueMap.get(x.getVariableName());
                        if (result != null) {
                            instruction.setX(result);
                        }
                    }
                }
                if(y != null) {
                    final Result result = valueMap.get(y.getVariableName());
                    if(result != null) {
                        instruction.setY(result);
                    }
                }

            }
        }
    }

    private void updateDominatedBy(List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            final Integer opcode = instruction.getOpcode();
            if(instruction.isPhi()) {
                valueMap.remove(instruction.getSymbol().getName());
                continue;
            }
            final String variableName = instruction.getX().getVariableName();
            if(instruction.isKill()) {
                valueMap.remove(variableName);
                continue;
            } else {
                final Result x = instruction.getX();
                final Result y = instruction.getY();
                if(opcode != OperationCode.move) {
                    if (x != null) {
                        final Result result = valueMap.get(x.getVariableName());
                        if (result != null) {
                            instruction.setX(result);
                        }
                    }
                }
                if(y != null) {
                    final Result result = valueMap.get(y.getVariableName());
                    if(result != null) {
                        instruction.setY(result);
                    }
                }

            }
        }
    }

    private void updateValueMap(Instruction instruction, String variableName, Result y) {
        if(y.kind().isVariable()) {
            final Result yValue = valueMap.get(y.getVariableName());
            if(yValue != null) {
                if(yValue.kind().isVariable()) {
                    updateValueMap(instruction, variableName, yValue);
                    return;
                }
                valueMap.put(variableName, yValue);
                instruction.setY(yValue);
                Printer.debugMessage("Copying for " + variableName + " in instruction number [" + instruction.getLocation() + "]");
            }
        } else if(y.kind().isConstant() || y.kind().isIntermediate()) {
            valueMap.put(variableName, y);
            instruction.setY(y);
            Printer.debugMessage("Copying for " + variableName + " in instruction number["+instruction.getLocation()+"]");
        } else {
            Printer.debugMessage(instruction);
        }
    }

    @Override
    public void finish() {
        super.finish();
        for (String s : valueMap.keySet()) {
            Printer.debugMessage(s);
        }
    }
}
