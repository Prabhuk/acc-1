package com.acc.graph;

import com.acc.constants.Kind;
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
public class CPWorker extends Worker {

    private Map<String, Result> valueMap = new HashMap<String, Result>();
    private List<Instruction> phiInstructions = new ArrayList<Instruction>();

    public CPWorker(SymbolTable symbolTable) {
        super(symbolTable);
    }

    @Override
    public void begin() {
        final List<Symbol> symbols = symbolTable.getSymbols();
        for (Symbol symbol : symbols) {
            if(symbol.getType().isVariable()) {
                final Result zero = new Result(Kind.CONSTANT);
                zero.value(0);
                valueMap.put(symbol.getName(), zero);
            }
        }

    }

    @Override
    public void visit(BasicBlock node) {
        final List<Instruction> instructions = node.getInstructions();
        final List<BasicBlock> dominatesOver = node.getDominatesOver();
        processInstructions(instructions);
        for (BasicBlock basicBlock : dominatesOver) {
            updateDominatedBy(basicBlock.getInstructions());
        }
        for (Instruction instruction : phiInstructions) {
            if (instruction.isPhi()) {
                updatePhiInstruction(instruction);
            }
        }
        valueMap = new HashMap<String, Result>();
    }

    private void updatePhiInstruction(Instruction instruction) {
        Result result = valueMap.get(instruction.getX().getUniqueIdentifier());
        if (result != null) {
            instruction.setX(result);
            valueMap.remove(instruction.getX().getUniqueIdentifier());
            valueMap.put(instruction.getSymbol().getName(), new Result(instruction.getSymbol()));
        }

        Result resulty = valueMap.get(instruction.getY().getUniqueIdentifier());
        if (resulty != null) {
            instruction.setY(resulty);
            valueMap.remove(instruction.getY().getUniqueIdentifier());
            valueMap.put(instruction.getSymbol().getName(), new Result(instruction.getSymbol()));
        }
    }

    private void processInstructions(List<Instruction> instructions) {
        for (Instruction instruction : instructions) {
            final Integer opcode = instruction.getOpcode();
            if(instruction.isPhi()) {
                valueMap.remove(instruction.getSymbol().getName());
                continue;
            }
            if(instruction.isKill()) {
                final String variableName = instruction.getX().getVariableName();
                valueMap.remove(variableName);
                continue;
            } else {
                final Result x = instruction.getX();
                final Result y = instruction.getY();
                if(opcode == OperationCode.move) {
                    final String variableName = instruction.getX().getVariableName();
                    updateValueMap(instruction, variableName, instruction.getX().getUniqueIdentifier(), y);
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
            if(OperationCode.end == opcode) {
                return;
            }
            if(instruction.isPhi()) {
                valueMap.remove(instruction.getSymbol().getName());
                phiInstructions.add(instruction);
//                updatePhiInstruction(instruction);
                continue;
            }

            if(instruction.isKill()) {
                final String variableName = instruction.getX().getVariableName();
                valueMap.remove(variableName);
                continue;
            } else {
                if(instruction.getX() == null && instruction.getY()==null) {
                    return;
                }
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

    private void updateValueMap(Instruction instruction, String variableName, String uniqueIdentifier, Result y) {
        if(y.kind().isVariable()) {
            final Result yValue = valueMap.get(y.getVariableName());
            if(yValue != null) {
                if(yValue.kind().isVariable()) {
                    updateValueMap(instruction, variableName, uniqueIdentifier, yValue);
                    return;
                }
                y = yValue;
            }
        }
        valueMap.put(variableName, y);
        valueMap.put(uniqueIdentifier, y);
        instruction.setY(y);
        instruction.setDeleted(true, "CP");
        symbolTable.removeSymbol(variableName, instruction.getLocation());
        Printer.debugMessage("Copying for " + variableName + " in instruction number[" + instruction.getLocation() + "]");

    }

    @Override
    public void finish() {
        super.finish();
        for (String s : valueMap.keySet()) {
            Printer.debugMessage(s);
        }
    }
}
