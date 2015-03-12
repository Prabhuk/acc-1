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
    private Map<Integer, Result> deletedMoves = new HashMap<Integer, Result>();

    public CPWorker(Parser parser) {
        super(parser);
    }

    @Override
    public void visit(BasicBlock node) {
        final List<Instruction> instructions = node.getInstructions();
        final List<BasicBlock> dominatesOver = node.getDominatesOver();
        List<String> removed = new ArrayList<String>();
        final Map<String, Result> thisNodeValues = processInstructions(removed, node, instructions);
        for (BasicBlock basicBlock : dominatesOver) {
            basicBlock.updateValueMap(thisNodeValues);
            basicBlock.updateExclude(node.getExclude());
            for (String s : removed) {
                node.getExclude().remove(s);
        }
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

    private Map<String, Result> processInstructions(List<String> removed, BasicBlock basicBlock, List<Instruction> instructions) {
        Map<String, Result> valueMap = basicBlock.getValueMap();
        List<String> exclude = basicBlock.getExclude();
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
                final List<Result> parameters = instruction.getParameters();
                if (opcode == OperationCode.move) {
                    final String variableName = instruction.getX().getVariableName();
                    updateValueMap(basicBlock.getValueMap(), instruction, variableName, instruction.getX().getUniqueIdentifier(), y);
                    exclude.remove(variableName);
                    removed.add(variableName);
                } else {
                    if (x != null) {
                        Result result = basicBlock.getValueMap().get(x.getVariableName());
                        if (result != null && !result.isVariable()) {
                            instruction.setX(result);
                        } else {
                            if(instruction.getX().isVariable()) {
                                final Result zeroIfUninitialized = getZeroIfUninitialized(instruction.getX(), exclude);
                                if(zeroIfUninitialized == null) {
                                    final Result zero = new Result(Kind.CONSTANT);
                                    zero.value(0);
                                    instruction.setX(zero);
                                }
                            }
                        }
                    }
                }

                if(parameters != null) {
                    Map<Result, Result> parameterMap = new LinkedHashMap<Result, Result>();
                    for (Result parameter : parameters) {
                        parameterMap.put(parameter, null);

                        Result result = basicBlock.getValueMap().get(parameter.getVariableName());
                        if (result != null && !result.isVariable()) {
                            parameterMap.put(parameter, result);
                        } else {
                            if(parameter.isVariable()) {
                                final Result zeroIfUninitialized = getZeroIfUninitialized(parameter, exclude);
                                if(zeroIfUninitialized == null) {
                                    final Result zero = new Result(Kind.CONSTANT);
                                    zero.value(0);
                                    parameterMap.put(parameter, zero);
                                }
                            }
                        }

                    }



                    List<Result> newParams = new ArrayList<Result>();
                    for (Result result : parameterMap.keySet()) {
                        if(parameterMap.get(result) != null) {
                            newParams.add(parameterMap.get(result));
                        } else {
                            newParams.add(result);
                        }
                    }
                    instruction.setParameters(newParams);
                }

                if (y != null) {
                    Result result = basicBlock.getValueMap().get(y.getVariableName());
                    if (result != null && !result.isVariable()) {
                        instruction.setY(result);
                    } else {
                        if(instruction.getY().isVariable()) {
                            final Result zeroIfUninitialized = getZeroIfUninitialized(instruction.getY(), exclude);
                            if(zeroIfUninitialized == null) {
                                final Result zero = new Result(Kind.CONSTANT);
                                zero.value(0);
                                instruction.setY(zero);
                            }
                        }
                    }
                }

            }
        }
        return valueMap;
    }

    private void updateValueMap(Map<String, Result> valueMap, Instruction instruction, String variableName, String uniqueIdentifier, Result y) {
//        if (!exclude.contains(variableName)) {

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
            deletedMoves.put(instruction.getLocation(), y);
            getSymbolTable().updateSymbol(variableName, y);
            Printer.debugMessage("Copying for " + variableName + " in instruction number[" + instruction.getLocation() + "]");
//        }
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

        Map<Integer, Result> update = new HashMap<Integer, Result>();

        for (Instruction instruction : instructions) {
            if (instruction.isDeleted()) {
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

    private Result updateDeleted(Map<Integer, Result> update, Instruction instruction, Result operand) {
        if (operand == null) {
            return operand;
        }
//        if(operand.isIntermediate()) {
//            final Result result = update.get(operand.getIntermediateLoation());
//            if(result != null) {
//                return result;
//            }
//        }
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
            if(getZeroIfUninitialized(operand, exclude) != null) {
                return operand;
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

    private Result getZeroIfUninitialized(Result operand, List<String> exclude) {
        final Computation computation = (Computation) parser;
        String programName = computation.getProgramName();
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
        if(exclude.contains(operand.getVariableName())) {
            return operand;
        }
        return null;
    }

    protected Result getTarget(Map<Integer, Result> remainingMoves, Result operand, Instruction instruction) {
        if (instruction.isPhi() && operand.isVariable()) {
            final Result result = remainingMoves.get(operand.getLocation());
            if (result != null) {
                return result;
            }
        }
//        if (operand != null && operand.isIntermediate()) {
//            return remainingMoves.get(operand.getIntermediateLoation());
//        }

        return null;
    }
}
