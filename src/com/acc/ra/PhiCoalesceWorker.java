package com.acc.ra;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.graph.Worker;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.util.AuxiliaryFunctions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 3/9/2015.
 */
public class PhiCoalesceWorker extends Worker{
    private final InterferenceGraph graph;
    private Set<GraphNode> nodes = new HashSet<GraphNode>();

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
            final Symbol targetSymbol = phi.getSymbol();
            final Result symbol = new Result(Kind.VAR);
            symbol.setLocation(targetSymbol.getSuffix());

            GraphNode phiNode = graph.getNodeForId(phi.getLocation());
            if(phiNode == null) {
                phiNode = new GraphNode(phi.getLocation()); //$TODO$ this is used for clustering the nodes within
                graph.addNode(phiNode);
            }

            if(graph.doesInterfere(operand1, operand2)) {
                introduceMove(phiNode, operand1, node.getLeft());
                introduceMove(phiNode, operand2, node.getRight());
            } else {
                if (graph.doesInterfere(phiNode, operand1)) {
                    introduceMove(phiNode, operand1, node.getLeft());
                }

                if (graph.doesInterfere(phiNode, operand2)) {
                    introduceMove(phiNode, operand2, node.getRight());
                }
            }
            coalesce(phiNode, node, operand1, operand2);
            //$TODO$ introduce value update to symbol table before deletion
            phi.setDeleted(true, "coalesced");
        }
    }

    public void coalesce(GraphNode phiNode, BasicBlock node, Result operand1, Result operand2) {

        if(operand1.isVariable()) {
            graph.coalesce(phiNode, graph.getNodeForId(operand1.getLocation()));
        }
        if(operand2.isVariable()) {
            graph.coalesce(phiNode, graph.getNodeForId(operand2.getLocation()));
        }

        if(operand1.isConstant()) {
            introduceMove(phiNode, operand1, node.getLeft());
        }
        if(operand2.isConstant()) {
            introduceMove(phiNode, operand2, node.getRight());
        }
    }

    protected void introduceMove(GraphNode phiNode, Result interferingResult, BasicBlock targetNode) {
        final BasicBlock oldCurrent = parser.getCode().getControlFlowGraph().getCurrentBlock();
        final Result x = new Result(Kind.REG); //$TODO$ update register number after coloring
        List<Instruction> instructions = targetNode.getInstructions();
        parser.getCode().getControlFlowGraph().setCurrentBlock(targetNode);
        int targetIndex = 0;
        if(!instructions.isEmpty()) {
            final Instruction instruction = instructions.get(instructions.size() - 1);
            targetIndex = parser.getCode().getInstructions().indexOf(instruction);
            if(instruction.getOpcode() >= OperationCode.bra && instruction.getOpcode() <= OperationCode.bgt) {
                targetIndex--;
            }
        }
        final Instruction addedInstruction = AuxiliaryFunctions.addInstruction(OperationCode.move, parser.getCode(), x, interferingResult, parser.getSymbolTable(), targetIndex);
        parser.getCode().getControlFlowGraph().setCurrentBlock(oldCurrent);
        phiNode.addToMoveInstructions(addedInstruction);
    }

    public Set<GraphNode> getNodes() {
        return nodes;
    }
}
