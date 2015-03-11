package com.acc.ra;

import com.acc.data.Instruction;
import com.acc.graph.DeleteInstructions;
import com.acc.graph.GraphHelper;
import com.acc.parser.Computation;
import com.acc.ra.GraphNode;
import com.acc.ra.InterferenceGraph;
import com.acc.ra.PhiCoalesceWorker;
import com.acc.structure.BasicBlock;

import java.util.*;

/**
 * Created by prabhuk on 2/18/2015.
 */
public class RegisterAllocator {

    private final Computation program;
    private final InterferenceGraph graph;
    private final Stack<GraphNode> nodeStack = new Stack<GraphNode>();
    private final Map<Integer, Integer> registerInfo = new HashMap<Integer, Integer>();
    private final Map<Integer, Integer> registerInfoAfterUpdate = new HashMap<Integer, Integer>();

    public RegisterAllocator(Computation program, InterferenceGraph graph) {
        this.program = program;
        this.graph = graph;
    }

    public void processPhis() {
        final PhiCoalesceWorker phiWorker = new PhiCoalesceWorker(program, graph);
        final BasicBlock rootNode = program.getCode().getControlFlowGraph().getRootBlock();
        new GraphHelper(phiWorker, rootNode);
        graph.sortNodesByNeighborsCount();
        final Iterator<GraphNode> iterator = graph.getNodes().iterator();
        while (iterator.hasNext()) {
            final GraphNode graphNode = iterator.next();
            if(graphNode.getNeighbors().size() <=8) {
                final Iterator<GraphNode> iterator2 = graph.getNodes().iterator();
                while (iterator2.hasNext()) {
                    final GraphNode targetNode = iterator2.next();
                    targetNode.getNeighbors().remove(graphNode);
                }
                nodeStack.push(graphNode);
                iterator.remove();
            } else {
                break;
            }
        }

//        final Iterator<GraphNode> iterator2 = graph.getNodes().iterator();
//        while (iterator2.hasNext()) {
//            final GraphNode targetNode = iterator2.next();
//            if(remove.contains(targetNode)) {
//                iterator2.remove();
//            }
//            targetNode.getNeighbors().remove(graphNode);
//        }

        graph.sortByCost();
        final Iterator<GraphNode> costIterator = graph.getNodes().iterator();
        while (costIterator.hasNext()) {
            final GraphNode next = costIterator.next();
            costIterator.remove();
            nodeStack.push(next);
        }


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
                    if(!graph.doesInterfere(null, existingNode, top)) {
                        existingColor = registerInfo.get(existingNode.getNodeId());
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


        //This iteration of Delete will actually have added instructions for compensated moves
        final DeleteInstructions reorder = new DeleteInstructions(program.getCode(), program);
        new GraphHelper(reorder, rootNode);
        final Map<Integer, Integer> oldNewLocations = reorder.getOldNewLocations();
        for (Integer old : registerInfo.keySet()) {
            final Integer newLocation = oldNewLocations.get(old);
            if(newLocation != null) {
                registerInfoAfterUpdate.put(newLocation, registerInfo.get(old));
            }
        }
        for (Integer integer : registerInfoAfterUpdate.keySet()) {
            System.out.println("Mapping key ["+integer+"] to register R["+registerInfoAfterUpdate.get(integer)+"]");
        }
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
