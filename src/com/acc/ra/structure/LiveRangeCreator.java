package com.acc.ra.structure;

import com.acc.constants.OperationCode;
import com.acc.data.BlockType;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.graph.GraphReverseTraversalHelper;
import com.acc.graph.Worker;
import com.acc.parser.Computation;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;
import com.acc.structure.ControlFlowGraph;
import com.acc.ui.OutputContents;
import com.acc.util.Printer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 3/4/2015.
 */
public class LiveRangeCreator extends Worker {

    private final OutputContents outputContents;
    private Set<Instruction> phis = new HashSet<Instruction>();

    public LiveRangeCreator(Parser parser, OutputContents contents) {
        super(parser);
        this.outputContents = contents;
    }


    @Override
    public void visit(BasicBlock node) {
        Set<Integer> liveRanges = new HashSet<Integer>();
        final Set<BasicBlock> children = node.getChildren();
        if (!node.isWhileHead()) {
            handleUnvisitedChildren(children);
        }
        for (BasicBlock child : children) {
            liveRanges.addAll(child.getLiveRanges());
        }

        if(node.isCall()) {
            //handle the function's liverange and add it to this block
            //$TODO$ function instruction numbers are currently beginning from zero which is wrong. It has to point to different numbers
            final Instruction callInstruction = node.getInstructions().get(0);
            final Computation functionProgram = outputContents.getProgram(callInstruction.getX().getVariableName());
            if(!functionProgram.equals(parser)) { //Recursion
                final LiveRangeCreator liveRangeWorker = new LiveRangeCreator(functionProgram, outputContents);
                final ControlFlowGraph functionCFG = functionProgram.getCode().getControlFlowGraph();
                new GraphReverseTraversalHelper(liveRangeWorker, functionCFG.getLastNode());
                liveRanges.addAll(functionCFG.getRootBlock().getLiveRanges());
            }
        }

        final List<Instruction> instructions = node.getInstructions();
        for (int i = instructions.size() - 1; i >= 0; i--) {

            final Instruction instruction = instructions.get(i);
            if(instruction.isPhi()) {
                phis.add(instruction);
            }
            final Integer opcode = instruction.getOpcode();
            final Integer operandCount = OperationCode.getOperandCount(opcode);
            if (operandCount > 0) {
                updateLiveRange(instruction, instruction.getX(), liveRanges);
            }
            if (operandCount > 1) {
                updateLiveRange(instruction, instruction.getY(), liveRanges);
            }
        }

        Printer.debugMessage("Live Ranges for BB:[" + node.getLabel() + "] are {");
        for (Integer liveRange : liveRanges) {
            Printer.debugMessage(String.valueOf(liveRange) + ",");
        }
        Printer.debugMessage("}");
        handleWhileBodyBlock(node, liveRanges);
        node.setLiveRanges(liveRanges);

    }

    protected void handleWhileBodyBlock(BasicBlock node, Set<Integer> liveRanges) {
        if (node.isWhileBody()) {
            node.setType(null);
            BasicBlock whileHead = getWhileHead(node.getParents());
            final Set<BasicBlock> allParents = whileHead.getParents();
            Set<BasicBlock> parent = new HashSet<BasicBlock>();
            parent.add(node);
            whileHead.setParents(parent);
            new GraphReverseTraversalHelper(this, whileHead, node);
            whileHead.setParents(allParents);
            liveRanges.addAll(whileHead.getLiveRanges());
            node.setType(BlockType.WHILE_BODY);
        }
    }

    protected BasicBlock getWhileHead(Set<BasicBlock> children) {
        BasicBlock whileHead = null;

        for (BasicBlock child : children) {
            if (child.isWhileHead()) {
                whileHead = child;
            }
        }

        return whileHead;
    }

    protected void handleUnvisitedChildren(Set<BasicBlock> children) {
        if (children.size() > 1) {
            List<BasicBlock> unvisited = new ArrayList<BasicBlock>();
            for (BasicBlock child : children) {
                if (!child.isVisited()) {
                    unvisited.add(child);
                }
            }
            if (unvisited.size() == 1) {
                visit(unvisited.get(0));
                unvisited.get(0).setVisited(true);
                addToNodes(unvisited.get(0));
            }
        }
    }

    private void updateLiveRange(Instruction instruction, Result result, Set<Integer> liveRanges) {
        if (result.isIntermediate()) {
            liveRanges.add(result.getIntermediateLoation());
        }
        liveRanges.remove(instruction.getLocation());
    }

    public Set<Instruction> getPhis() {
        return phis;
    }
}
