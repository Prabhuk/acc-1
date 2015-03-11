package com.acc.graph;

import com.acc.constants.Kind;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;

import java.util.*;

/**
 * Created by prabhuk on 3/11/2015.
 */
public class PhiMapper extends Worker {

    public PhiMapper(Parser parser) {
        super(parser);
    }

    @Override
    public void visit(BasicBlock node) {
        if(node.isWhileHead()) {
            final Collection<Instruction> allPhiInstructions = node.getJoinBlock().getAllPhiInstructions();
            final Map<String, Result> phiMap = new HashMap<String, Result>();
            for (Instruction allPhiInstruction : allPhiInstructions) {
                final String name = allPhiInstruction.getSymbol().getName();
                final Result result = new Result(Kind.INTERMEDIATE);
                result.setIntermediateLoation(allPhiInstruction.getLocation());
                phiMap.put(allPhiInstruction.getSymbol().getName(), result);
            }
            BasicBlock end = null;
            final Set<BasicBlock> parents = node.getJoinBlock().getParents();
            for (BasicBlock parent : parents) {
                if(!parent.equals(node)) {
                    end = parent;
                }
            }
            new GraphHelper(new UpdateWhileBodyWorker(parser, phiMap), node, end);
        }
    }


    protected Result getPhiTarget(Map<Integer, Result> phis, Result operand, Instruction instruction) {
        if(operand == null) {
            return null;
        }
        if (operand.isVariable()) {
            if(operand.getLocation() != null) {
                if (instruction.getLocation() == operand.getLocation()) { //avoiding self reference
                    return operand;
                }
                final Result result = phis.get(operand.getLocation());
                if (result != null) {
                    return result;
                }
            }
        }
        if (operand.isIntermediate()) {
            final Result result = phis.get(operand.getIntermediateLoation());
            if(result != null) {
                return result;
            }
        }

        return operand;
    }


    private class UpdateWhileBodyWorker extends Worker {

        private final Map<String, Result> phiMap;

        public UpdateWhileBodyWorker(Parser parser, Map<String, Result> phiMap) {
            super(parser);
            this.phiMap = phiMap;
        }

        @Override
        public void visit(BasicBlock node) {
            final List<Instruction> instructions = node.getInstructions();
            for (Instruction instruction : instructions) {
                instruction.setX(updatePhiReferences(instruction, phiMap, instruction.getX()));
                instruction.setY(updatePhiReferences(instruction, phiMap, instruction.getY()));

            }
        }

        private Result updatePhiReferences(Instruction instruction, Map<String, Result> phiMap, Result operand) {
            if(operand == null) {
                return operand;
            }
            if(instruction.isPhi()) {
                return operand;
            }
            if(operand.isVariable()) {
                final Result result = phiMap.get(operand.getVariableName());
                if(result == null) {
                    return operand;
                }
                return result;
            }
            return operand;
        }
    }

}
