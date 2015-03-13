package com.acc.graph;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 3/11/2015.
 */
public class MoveFinder extends Worker {

    private final BasicBlock join;
    private final BasicBlock start;
    private boolean reachedEnd = false;
    private List<Result> operands = new ArrayList<Result>();
    private List<Instruction> moves = new ArrayList<Instruction>();
    private boolean started = false;


    public MoveFinder(Parser parser, BasicBlock join, BasicBlock start) {
        super(parser);
        this.join = join;
        this.start = start;
    }

    @Override
    public void visit(BasicBlock node) {
        if(reachedEnd) {
            return;
        }

        if(node.equals(start)) {
            started = true;
        }

        if(!started) {
            return;
        }

        if(node.equals(join)) {
            reachedEnd = true;
        }

//        if(!node.isDominatingOver(join)) {
//            return;
//        }
        final List<Instruction> instructions = node.getInstructions();
        for (Instruction instruction : instructions) {
            if(instruction.getOpcode() == OperationCode.move) {
                moves.add(instruction);
            }
            if(instruction.isPhi()) {
                moves.add(instruction);
            }
        }
    }

    public List<Instruction> getMoves() {
        return moves;
    }
}
