package com.acc.graph;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;

import java.util.List;

/**
 * Created by prabhuk on 3/11/2015.
 */
public class PhiOperandFinder extends Worker {

    private final BasicBlock join;
    private final String variableName;
    private boolean reachedEnd = false;
    private Result y;


    public PhiOperandFinder(Parser parser, BasicBlock join, String variableName) {
        super(parser);
        this.join = join;
        this.variableName = variableName;
        this.y = new Result(Kind.CONSTANT);
        this.y.value(0);
    }

    @Override
    public void visit(BasicBlock node) {
        if(reachedEnd) {
            return;
        }

        if(node.equals(join)) {
            reachedEnd = true;
        }

        if(!node.isDominatingOver(join)) {
            return;
        }
        final List<Instruction> instructions = node.getInstructions();
        for (Instruction instruction : instructions) {
            if(instruction.getOpcode() == OperationCode.move) {
                if(instruction.getX().getVariableName().equals(variableName)) {
                    this.y = instruction.getY();
                }
            } else if(instruction.isPhi()) {
                if(instruction.getSymbol().getName().equals(variableName)) {
                    this.y = new Result(Kind.INTERMEDIATE);
                    this.y.setIntermediateLoation(instruction.getLocation());
                }
            }
        }
    }

    public Result getOperand() {
        return y;
    }
}
