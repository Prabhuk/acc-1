package com.acc.graph;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.parser.Computation;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.util.Printer;

import java.util.*;

/**
 * Created by prabhuk on 2/23/2015.
 */
public class CPWorker extends Worker {

    private Map<String, Result> valueMap = new HashMap<String, Result>();
    private List<String> exclude = new ArrayList<String>();
    private Set<Instruction> phiInstructions = new HashSet<Instruction>();

    public CPWorker(Parser parser) {
        super(parser);
    }

    @Override
    public void visit(BasicBlock node) {
        final List<Instruction> instructions = node.getInstructions();
        final List<BasicBlock> dominatesOver = node.getDominatesOver();
        final Map<String, Result> thisNodeValues = processInstructions(node, instructions);
        for (BasicBlock basicBlock : dominatesOver) {
            basicBlock.updateValueMap(thisNodeValues);
        }
        for (Instruction instruction : phiInstructions) {
            if (instruction.isPhi()) {
                updatePhiInstruction(instruction);
            }
        }
        valueMap.putAll(thisNodeValues);
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

    private Map<String, Result> processInstructions(BasicBlock basicBlock, List<Instruction> instructions) {
        Map<String, Result> valueMap = basicBlock.getValueMap();
        for (Instruction instruction : instructions) {
            final Integer opcode = instruction.getOpcode();
            if (instruction.isPhi()) {
                phiInstructions.add(instruction);
                final Result result = new Result(Kind.INTERMEDIATE);
                result.setIntermediateLoation(instruction.getLocation());
                valueMap.put(instruction.getSymbol().getName(), result);
                continue;
            }
            if (instruction.isKill()) {
                final String variableName = instruction.getX().getVariableName();
                valueMap.remove(variableName);
                exclude.add(variableName);
                continue;
            } else {
                final Result x = instruction.getX();
                final Result y = instruction.getY();
                if (opcode == OperationCode.move) {
                    final String variableName = instruction.getX().getVariableName();
                    updateValueMap(basicBlock.getValueMap(), instruction, variableName, instruction.getX().getUniqueIdentifier(), y);
                } else {
                    if (x != null) {
                        final Result result = basicBlock.getValueMap().get(x.getVariableName());
                        if (result != null && !result.isVariable()) {
                            instruction.setX(result);
                        }
                    }
                }
                if (y != null) {
                    final Result result = basicBlock.getValueMap().get(y.getVariableName());
                    if (result != null && !result.isVariable()) {
                        instruction.setY(result);
                    }
                }

            }
        }
        return valueMap;
    }

    private void updateValueMap(Map<String, Result> valueMap, Instruction instruction, String variableName, String uniqueIdentifier, Result y) {
        if (!exclude.contains(variableName)) {

            if (y.isVariable()) {
                final Result yValue = valueMap.get(y.getVariableName());
                if (yValue != null) {
                    if (yValue.isVariable()) {
                        updateValueMap(valueMap, instruction, variableName, uniqueIdentifier, yValue);
                        return;
                    }
                    y = yValue;
                }
            }
            valueMap.put(variableName, y);
            valueMap.put(uniqueIdentifier, y);
            instruction.setY(y);
            instruction.setDeleted(true, "CP");
            getSymbolTable().updateSymbol(variableName, y);
            Printer.debugMessage("Copying for " + variableName + " in instruction number[" + instruction.getLocation() + "]");
        }
    }

    @Override
    public void finish() {
        super.finish();
        for (String s : valueMap.keySet()) {
            Printer.debugMessage(s);
        }

        final List<Instruction> instructions = parser.getCode().getInstructions();
        Map<Integer, Result> remainingMoves = new HashMap<Integer, Result>();
        final List<Instruction> phis = new ArrayList<Instruction>();
        for (Instruction instruction : instructions) {
            if (instruction.getOpcode() == OperationCode.move && !instruction.isDeleted()) {
                final Result target = instruction.getY();
                final Result x = instruction.getX();
                if (x.isVariable()) {
                    final Integer key = x.getLocation();
                    remainingMoves.put(key, target);
                    instruction.setDeleted(true, "CP");
                }
            }
            if(instruction.isPhi()) {
                phis.add(instruction);
            }
        }

        for (Instruction instruction : instructions) {
            Result target = getTarget(remainingMoves, instruction.getX(), instruction);
            if (target != null) {
                instruction.setX(target);
            }
            target = getTarget(remainingMoves, instruction.getY(), instruction);
            if (target != null) {
                instruction.setY(target);
            }
        }

        for (Instruction instruction : instructions) {
            instruction.setX(updatePhiReferences(instruction.getX()));
            instruction.setY(updatePhiReferences(instruction.getY()));
        }

        List<Instruction> deleted = new ArrayList<Instruction>();
        Map<Integer, Result> update = new HashMap<Integer, Result>();

        for (Instruction instruction : instructions) {
            if (instruction.isDeleted()) {
                deleted.add(instruction);
                if(!instruction.getY().isVariable()) {
                    update.put(instruction.getLocation(), instruction.getY());
                }
                continue;
            }
            instruction.setX(updateFirstOccurence(instruction.getX()));
            instruction.setY(updateFirstOccurence(instruction.getY()));
        }

        for (Instruction instruction : instructions) {
            instruction.setX(updateDeleted(update, instruction, instruction.getX()));
            instruction.setY(updateDeleted(update, instruction, instruction.getY()));
        }

        for (Instruction phi : phis) {
            if(phi.getX().isVariable()) {
                final Result x = new Result(Kind.INTERMEDIATE);
                x.setIntermediateLoation(phi.getX().getLocation());
                phi.setX(x);
            }
            if(phi.getY().isVariable()) {
                final Result y = new Result(Kind.INTERMEDIATE);
                y.setIntermediateLoation(phi.getY().getLocation());
                phi.setY(y);
            }
        }
    }

    private Result updatePhiReferences(Result operand) {
        if (operand == null) {
            return operand;
        }
        return operand;
    }

    private Result updateDeleted(Map<Integer, Result> update, Instruction instruction, Result operand) {
        if (operand == null) {
            return operand;
        }
        if(operand.isIntermediate()) {
            final Result result = update.get(operand.getIntermediateLoation());
            if(result != null) {
                return result;
            }
        }
        if(instruction.isPhi() && operand.isVariable()) {
            final Result result = update.get(operand.getLocation());
            if(result != null) {
                return result;
            }
        }
        return operand;
    }

    protected Result updateFirstOccurence(Result operand) {
        if (operand != null && operand.isVariable()) {
            final Computation computation = (Computation) parser;
            final String programName = computation.getProgramName();
            final Symbol recentOccurence = parser.getSymbolTable().getRecentOccurence(operand.getVariableName());
            if (!programName.equals("main")) {
                if (recentOccurence.isGlobal()) {
                    return operand;
                }
                final List<String> argumentNames = computation.getFormalParams();
                for (String argumentName : argumentNames) {
                    if (operand.getVariableName().equals(argumentName)) {
                        return operand;
                    }
                }
            }
            if (recentOccurence.getSuffix() != -1) {
                return operand;
            }
            final Result zero = new Result(Kind.CONSTANT);
            zero.value(0);
            return zero;
        }
        return operand;
    }

    protected Result getTarget(Map<Integer, Result> remainingMoves, Result operand, Instruction instruction) {
        if (instruction.isPhi() && operand.isVariable()) {
            final Result result = remainingMoves.get(operand.getLocation());
            if (result != null) {
                return result;
            }
        }
        if (operand != null && operand.isIntermediate()) {
            return remainingMoves.get(operand.getIntermediateLoation());
        }

        return null;
    }
}
