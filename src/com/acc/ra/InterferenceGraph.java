package com.acc.ra;

import com.acc.data.Instruction;
import com.acc.data.Result;

import java.util.*;

/**
 * Created by prabhuk on 3/5/2015.
 */
public class InterferenceGraph {
    private List<GraphNode> nodes = new ArrayList<GraphNode>();
    private final Map<Integer, GraphNode> nodeById = new HashMap<Integer, GraphNode>();
    private GraphNode rootNode;

    public GraphNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(GraphNode rootNode) {
        this.rootNode = rootNode;
    }

    public void addNode(GraphNode node) {
        if(node==null){
            return;
        }
        if(nodes.indexOf(node) != -1) {
            return;
        }
        nodes.add(node);
        nodeById.put(node.getNodeId(), node);
    }

    public void sortByCost() {
        Collections.sort(nodes, new Comparator<GraphNode>() {
            @Override
            public int compare(GraphNode o1, GraphNode o2) {
                return o1.getNeighbors().size() - o2.getNeighbors().size();
            }
            //$TODO$ add the loop level weight
        });
    }

    public void sortDescendingByClusterSize() {
        Collections.sort(nodes, new Comparator<GraphNode>() {
            @Override
            public int compare(GraphNode o1, GraphNode o2) {
                return o2.getClustered().size() - o1.getClustered().size();
            }
            //$TODO$ add the loop level weight
        });
    }


    public boolean doesInterfere(Result operand1, Result operand2) {
        if(operand1.isConstant() || operand2.isConstant()) {
            return false;
        }
        final GraphNode operand1Node = getNodeForOperand(operand1);
        final GraphNode operand2Node = getNodeForOperand(operand2);
        return doesInterfere(operand1Node, operand2Node);
    }

    public boolean doesInterfere(GraphNode node, Result operand) {
        if(operand.isConstant()) {
            return false;
        }
        final GraphNode operandNode = getNodeForOperand(operand);
        return doesInterfere(node, operandNode);
    }

    protected GraphNode getNodeForOperand(Result operand) {
        int node1key = getSearchKey(operand);
        GraphNode nodeForId = getNodeForId(node1key);
        return nodeForId;
    }

    protected boolean doesInterfere(GraphNode node1, GraphNode node2) {
        if(node1.equals(node2)) {
            return false;
        }
        final Set<GraphNode> nodeNeighbors = node1.getNeighbors();
        final Set<GraphNode> operandNeighbors = node2.getNeighbors();

        if(nodeNeighbors.contains(node2)) {
            return true;
        }

        if(operandNeighbors.contains(node1)) {
            return true;
        }

        return false;
    }

    protected int getSearchKey(Result operand) {
        if(!operand.isIntermediate() && !operand.isVariable()) {
            return -1;
        }
        if(operand.isIntermediate()) {
            return operand.getIntermediateLoation();
        } else {
            return operand.getLocation();
        }
    }

    public GraphNode getNodeForId(Integer id) {
        return nodeById.get(id);
    }

    public Set<GraphNode> coalesce(GraphNode node1, GraphNode node2) {
        final Set<GraphNode> node1Neighbors = node1.getNeighbors();
        final Set<GraphNode> node2Neighbors = node2.getNeighbors();
        node1Neighbors.addAll(node2Neighbors);
        for (GraphNode node : nodes) {
            if(node.getNeighbors().contains(node2)) {
                node.addNeighbor(node1);
            }
        }
        node1.addToCluster(node2);

        final Set<Instruction> moveInstructions = node2.getMoveInstructions();
        for (Instruction moveInstruction : moveInstructions) {
            node2.addToMoveInstructions(moveInstruction);
        }
        nodes.remove(node2);
        return node1.getClustered();
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

}
