package com.acc.ra;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.BlockType;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.graph.Worker;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;
import com.acc.util.AuxiliaryFunctions;

import java.util.*;

/**
 * Created by prabhuk on 3/9/2015.
 */
public class PhiCoalesceWorker extends Worker{
    private final InterferenceGraph graph;
    private Set<GraphNode> nodes = new HashSet<GraphNode>();
    private Set<Instruction> deletedPhis = new HashSet<Instruction>();

    public PhiCoalesceWorker(Parser parser, InterferenceGraph graph) {
        super(parser);
        this.graph = graph;
    }

    @Override
    public void visit(BasicBlock node) {
        final List<Instruction> _instructions = node.getInstructions();
        final List<Instruction> instructions = new ArrayList<Instruction>();
        instructions.addAll(_instructions); //Concurrent modification hack
        for (Instruction phi : instructions) {
            if(!phi.isPhi()) {
                continue;
            }
            final Result operand1 = phi.getX();
            final Result operand2 = phi.getY();

            GraphNode phiNode = graph.getNodeForId(phi.getLocation());
            if(phiNode == null) {
                phiNode = new GraphNode(phi.getLocation()); //$TODO$ this is used for clustering the nodes within
                graph.addNode(phiNode);
            }

            if(graph.doesInterfere(operand1, operand2)) {
                introduceMoveLeft(phiNode, operand1, node);
                introduceMoveRight(phiNode, operand2, node);
            } else {
                if (graph.doesInterfere(phiNode, operand1)) {
                    introduceMoveLeft(phiNode, operand1, node);
                }
                if (graph.doesInterfere(phiNode, operand2)) {
                    introduceMoveRight(phiNode, operand2, node);
                }
            }
            coalesce(phiNode, node, operand1, operand2);
            //$TODO$ introduce value update to symbol table before deletion
            phi.setDeleted(true, "coalesced");
            deletedPhis.add(phi);
        }
    }

    public void coalesce(GraphNode phiNode, BasicBlock node, Result operand1, Result operand2) {

        if(operand1.isIntermediate()) {
            graph.coalesce(phiNode, graph.getNodeForId(operand1.getIntermediateLoation()));
        }
        if(operand1.isVariable()) {
            graph.coalesce(phiNode, graph.getNodeForId(operand1.getLocation()));
        }
        if(operand2.isVariable()) {
            graph.coalesce(phiNode, graph.getNodeForId(operand2.getLocation()));
        }
        if(operand2.isIntermediate()) {
            graph.coalesce(phiNode, graph.getNodeForId(operand2.getIntermediateLoation()));
        }
    }

    protected void introduceMoveRight(GraphNode phiNode, Result interferingResult, BasicBlock node) {
        final Result x = new Result(Kind.REG);
        final BasicBlock oldCurrent = parser.getCode().getControlFlowGraph().getCurrentBlock();
        BasicBlock targetNode = node.getRight();
        if(node.isWhileHead()) {
            final List<Instruction> instructions = targetNode.getInstructions();
            final Instruction instruction = instructions.get(instructions.size() - 1);
            int target = parser.getCode().getInstructions().indexOf(instruction);
            if(instruction.getOpcode() != OperationCode.bra) {
                target++;
            }
            parser.getCode().getControlFlowGraph().setCurrentBlock(targetNode);
            final Instruction addedInstruction = AuxiliaryFunctions.addInstruction(OperationCode.move, parser.getCode(), x,
                    interferingResult, parser.getSymbolTable(), target);
            parser.getCode().getControlFlowGraph().setCurrentBlock(oldCurrent);
            phiNode.addToMoveInstructions(addedInstruction);
        } else {
            handleIfPhiMove(phiNode, interferingResult, targetNode);
        }

    }

    protected void introduceMoveLeft(GraphNode phiNode, Result interferingResult, BasicBlock node) {
        final Result x = new Result(Kind.REG);
        final BasicBlock oldCurrent = parser.getCode().getControlFlowGraph().getCurrentBlock();
        BasicBlock targetNode = node.getLeft();
        if(node.isWhileHead()) {
            targetNode = node;
            final Instruction instruction = node.getInstructions().get(0);
            parser.getCode().getControlFlowGraph().setCurrentBlock(targetNode);
            int target = parser.getCode().getInstructions().indexOf(instruction);
            final Instruction addedInstruction = AuxiliaryFunctions.addInstruction(OperationCode.move, parser.getCode(), x,
                    interferingResult, parser.getSymbolTable(), target);
            parser.getCode().getControlFlowGraph().setCurrentBlock(oldCurrent);
            phiNode.addToMoveInstructions(addedInstruction);
        } else {
            handleIfPhiMove(phiNode, interferingResult, targetNode);

        }
    }

    private void handleIfPhiMove(GraphNode phiNode, Result interferingResult,BasicBlock targetNode) {
        List<Instruction> instructions = targetNode.getInstructions();
        Instruction instruction = instructions.get(instructions.size() - 1);
        int target = parser.getCode().getInstructions().indexOf(instruction);
        if(instruction.getOpcode() == OperationCode.noop) {
            if(instructions.size() > 1) {
                target--;
                instruction = instructions.get(instructions.size() - 1);
            }
        }
        if(instruction.getOpcode() == OperationCode.bra) {
            if(instruction.getLocation() > instruction.getX().value()) {
                target++;
            }
        } else if(instruction.getOpcode() > OperationCode.bra && instruction.getOpcode() <= OperationCode.bgt) {
            target--; //insert before cmp
        }
        final Result x = new Result(Kind.REG);
        parser.getCode().getControlFlowGraph().setCurrentBlock(targetNode);
        final BasicBlock oldCurrent = parser.getCode().getControlFlowGraph().getCurrentBlock();
        final Instruction addedInstruction = AuxiliaryFunctions.addInstruction(OperationCode.move, parser.getCode(), x,
                interferingResult, parser.getSymbolTable(), target);
        parser.getCode().getControlFlowGraph().setCurrentBlock(oldCurrent);
        phiNode.addToMoveInstructions(addedInstruction);

    }


    public Set<Instruction> getDeletedPhis() {
        return deletedPhis;
    }
}
