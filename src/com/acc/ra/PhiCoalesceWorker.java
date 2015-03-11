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
        final List<Instruction> instructions = node.getInstructions();
        for (Instruction phi : instructions) {
            if(!phi.isPhi()) {
                continue;
            }
            final Result operand1 = phi.getX();
            final Result operand2 = phi.getY();
            final Symbol targetSymbol = phi.getSymbol();
            final Result symbol = new Result(Kind.VAR);
            symbol.setLocation(targetSymbol.getSuffix());

            if(!graph.doesInterfere(symbol, operand1, operand2)) {
                coalesce(node, symbol, operand1, operand2);
            } else {
                if(graph.doesInterfere(symbol, operand1)) {
                    introduceMove(symbol, node.getLeft(), operand1);
                }
                if(graph.doesInterfere(symbol, operand2)) {
                    introduceMove(symbol, node.getRight(), operand2);
                }
//                if(graph.doesInterfere(operand1, operand2)) {
//                }

//                nodes.add(graph.getNodeForId(graph.getSearchKey(operand1)));
//                nodes.add(graph.getNodeForId(graph.getSearchKey(operand2)));
//                nodes.add(graph.getNodeForId(targetSymbol.getSuffix()));
            }
            phi.setDeleted(true, "coalesced");
        }
    }

    public void coalesce(BasicBlock node, Result symbol, Result operand1, Result operand2) {
        if(operand1.isVariable() && operand2.isVariable()) {
            final GraphNode node1 = graph.getNodeForId(operand1.getLocation());
            if(node1 != null) { //Self targeted PHIs
                final Set<GraphNode> cluster = graph.coalesce(node1, graph.getNodeForId(operand2.getLocation()));
            }
//            nodes.add(node1);
        }
        if(operand1.isConstant()) {
            if(operand2.isVariable()) {
                introduceMove(operand2, node.getLeft(), operand1);
            } else {
                introduceMove(symbol, node.getLeft(), operand1);
            }
        }
        if(operand2.isConstant()) {
            if(operand1.isVariable()) {
                introduceMove(operand1, node.getRight(), operand2);
            } else {
                introduceMove(symbol, node.getRight(), operand2);
            }
        }
    }

    protected void introduceMove(Result symbol, BasicBlock targetNode, Result operand) {
        final BasicBlock oldCurrent = parser.getCode().getControlFlowGraph().getCurrentBlock();

        final Result x = new Result(Kind.REG); //$TODO$ update register number after coloring
        List<Instruction> instructions = targetNode.getInstructions();
        while (instructions.isEmpty()) {
            final Set<BasicBlock> parents = targetNode.getParents();
            if(parents == null || parents.isEmpty()) {
                break;
            }
            for (BasicBlock parent : parents) {
                targetNode = parent;
                instructions = parent.getInstructions();
                break;
            } //$TODO$ what happens on more than one parent
        }

        parser.getCode().getControlFlowGraph().setCurrentBlock(targetNode);
        int targetIndex = 0;
        if(!instructions.isEmpty()) {
            final Instruction instruction = instructions.get(instructions.size() - 1);
            targetIndex = parser.getCode().getInstructions().indexOf(instruction);
            if(instruction.getOpcode() >= OperationCode.bra && instruction.getOpcode() <= OperationCode.bgt) {
                targetIndex--;
            }
//            throw new RuntimeException("Where to insert move?");
//            return; //$TODO$ bug case
        }

        final Instruction addedInstruction = AuxiliaryFunctions.addInstruction(OperationCode.moveRegister, parser.getCode(), x, operand, parser.getSymbolTable(), targetIndex);
        parser.getCode().getControlFlowGraph().setCurrentBlock(oldCurrent);

        final GraphNode node = graph.getNodeForId(symbol.getLocation());
        if(node != null) {
            node.addToMoveInstructions(addedInstruction);
        }
    }

    public Set<GraphNode> getNodes() {
        return nodes;
    }
}
