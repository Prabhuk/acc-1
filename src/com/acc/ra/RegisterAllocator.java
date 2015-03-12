package com.acc.ra;

import com.acc.constants.Kind;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.graph.DeleteInstructions;
import com.acc.graph.GraphHelper;
import com.acc.parser.Computation;
import com.acc.structure.BasicBlock;

import java.util.*;

/**
 * Created by prabhuk on 2/18/2015.
 */
public class RegisterAllocator {

    private final Computation parser;
    private final InterferenceGraph graph;
    private final Stack<GraphNode> nodeStack = new Stack<GraphNode>();
    private final Map<Integer, Integer> registerInfo = new HashMap<Integer, Integer>();
    private final Map<Integer, Integer> registerInfoAfterUpdate = new HashMap<Integer, Integer>();

    public RegisterAllocator(Computation parser, InterferenceGraph graph) {
        this.parser = parser;
        this.graph = graph;
    }

    public void processPhis() {
        final PhiCoalesceWorker phiWorker = new PhiCoalesceWorker(parser, graph);
        final BasicBlock rootNode = parser.getCode().getControlFlowGraph().getRootBlock();
        new GraphHelper(phiWorker, rootNode);


        graph.sortByCost();
        final Iterator<GraphNode> costIterator = graph.getNodes().iterator();
        while (costIterator.hasNext()) {
            final GraphNode next = costIterator.next();
            removeFromGraph(costIterator,next);
            nodeStack.push(next);
        }

//        sortAndAddToStack(clusteredNodes);
        int colorNumber = 0;
        final List<GraphNode> existingNodes = graph.getNodes();
        while (!nodeStack.isEmpty()) {
            GraphNode top = nodeStack.pop();
            if(existingNodes.isEmpty()) {
                existingNodes.add(top);
                updateRegisterInfo(top, colorNumber, existingNodes);
                colorNumber++;
            } else {
                existingNodes.add(top);
                final Set<GraphNode> neighbors = top.getNeighbors();
                for (GraphNode neighbor : neighbors) {
                    neighbor.addNeighbor(top);
                }
                int existingColor = -1;
                for (GraphNode existingNode : existingNodes) {
                    if(!existingNode.equals(top) && !graph.doesInterfere(existingNode, top)) {
                        existingColor = registerInfo.get(existingNode.getNodeId()); //$TODO$ taking too much time: needs to be optimized
                        break;
                    }
                }
                if(existingColor != -1) {
                    updateRegisterInfo(top, existingColor, existingNodes);
                } else {
                    updateRegisterInfo(top, colorNumber, existingNodes);
                    colorNumber++;
                }
            }
        }
        System.out.println("Total colors used : " + colorNumber);

        final List<Instruction> instructions = parser.getCode().getInstructions();
        final Iterator<Instruction> iterator = instructions.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            final Instruction instruction = iterator.next();
            if(instruction.isDeleted()) {
                iterator.remove();
            }
        }
//        final Set<Instruction> deletedPhis = phiWorker.getDeletedPhis();
//        List<Integer> deletedPhiLocations = new ArrayList<Integer>();
//        for (Instruction deletedPhi : deletedPhis) {
//            deletedPhiLocations.add(deletedPhi.getLocation());
//        }
//
//        final List<Instruction> instructions = parser.getCode().getInstructions();
//        for (Instruction instruction : instructions) {
//            instruction.setX(phiReference(deletedPhiLocations, instruction.getX()));
//            instruction.setY(phiReference(deletedPhiLocations, instruction.getY()));
//        }
//        //This iteration of Delete will actually have added instructions for compensated moves
//        final DeleteInstructions reorder = new DeleteInstructions(parser.getCode(), parser);
//        new GraphHelper(reorder, rootNode);
//        final Map<Integer, Integer> oldNewLocations = reorder.getOldNewLocations();
//        for (Integer old : registerInfo.keySet()) {
//            final Integer newLocation = oldNewLocations.get(old);
//            if(newLocation != null) {
//                registerInfoAfterUpdate.put(newLocation, registerInfo.get(old));
//            }
//        }
        for (Integer integer : registerInfo.keySet()) {
            System.out.println("Mapping key ["+integer+"] to register R["+registerInfo.get(integer)+"]");
        }
    }

    protected Result phiReference(List<Integer> deletedPhiLocations, Result operand) {
        if(operand != null && operand.isIntermediate()) {
            if(deletedPhiLocations.contains(operand.getIntermediateLoation())) {
                final Result reg = new Result(Kind.REG);
                reg.regNo(registerInfo.get(operand.getIntermediateLoation()));
                return reg;
            }
        }
        return operand;
    }

    protected void sortAndAddToStack(List<GraphNode> clusteredNodes) {
        Collections.sort(clusteredNodes, new Comparator<GraphNode>() {
            @Override
            public int compare(GraphNode o1, GraphNode o2) {
                return o1.getClustered().size() - o2.getClustered().size();
            }
        });

        for (GraphNode clusteredNode : clusteredNodes) {
            nodeStack.push(clusteredNode);
        }
    }

    protected void removeFromGraph(Iterator<GraphNode> nodesIterator, GraphNode next) {
        final Iterator<GraphNode> neighborsRemoverIterator = graph.getNodes().iterator();
        while (neighborsRemoverIterator.hasNext()) {
            final GraphNode targetNode = neighborsRemoverIterator.next();
            targetNode.getNeighbors().remove(next);
        }
        nodesIterator.remove();
    }

    protected void updateRegisterInfo(GraphNode top, int colorNumber, List<GraphNode> existingNodes) {
        if(registerInfo.get(top.getNodeId()) != null) {
            return; //Already colored
        }
        registerInfo.put(top.getNodeId(), colorNumber);
        final Set<GraphNode> clustered = top.getClustered();
        for (GraphNode graphNode : clustered) {
            registerInfo.put(graphNode.getNodeId(), colorNumber);
            existingNodes.add(graphNode);
        }
        final Set<Instruction> moveInstructions = top.getMoveInstructions();
        for (Instruction moveInstruction : moveInstructions) {
            moveInstruction.getX().regNo(colorNumber);
        }
    }

    public Map<Integer, Integer> getRegisterInfoAfterUpdate() {
        return registerInfoAfterUpdate;
    }
}
