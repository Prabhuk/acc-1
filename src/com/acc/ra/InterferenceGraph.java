package com.acc.ra;

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
//        nodes.remove(null);
        Collections.sort(nodes, new Comparator<GraphNode>() {
            @Override
            public int compare(GraphNode o1, GraphNode o2) {
                if(o1.getClustered().size() != o2.getClustered().size()) {
                    return o1.getClustered().size() - o2.getClustered().size();
                }
                return o1.getNeighbors().size() - o2.getNeighbors().size();
            }
            //$TODO$ add the loop level weight
        });
    }

    public void sortNodesByNeighborsCount() {
//        nodes.remove(null);
        Collections.sort(nodes, new Comparator<GraphNode>() {
            @Override
            public int compare(GraphNode o1, GraphNode o2) {
                return o1.getNeighbors().size() - o2.getNeighbors().size();
            }
            //$TODO$ add the loop level weight
        });
    }


    public boolean doesInterfere(Result operand1, Result operand2) {
        if(operand1.isConstant() || operand2.isConstant()) {
            return false;
        }
        int nodeId = getSearchKey(operand1);
        int node2Id = getSearchKey(operand2);
        for (GraphNode node : nodes) {
            if(nodeId == node.getNodeId()) {
                final Set<GraphNode> neighbors = node.getNeighbors();
                for (GraphNode neighbor : neighbors) {
                    if(node2Id == neighbor.getNodeId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //For Phi Instructions
    public boolean doesInterfere(Result symbol, Result operand1, Result operand2) {

        if(operand1.isConstant() && operand2.isConstant()) {
            return false;
        }

        int node1key = getSearchKey(operand1);
        int node2key = getSearchKey(operand2);
        final GraphNode node1 = getNodeForId(node1key);
        final GraphNode node2 = getNodeForId(node2key);

        return doesInterfere(symbol, node1, node2);

    }

    public boolean doesInterfere(Result symbol, GraphNode node1, GraphNode node2) {
        if(node1 != null && node2 != null) {
            if(node1.equals(node2)) {
                return true;
            }
            final Set<GraphNode> neighbors = node1.getNeighbors();
            if(neighbors.contains(node2)) {
                return true;
            }
            if(symbol != null) {
                final GraphNode symbolNode = getNodeForId(symbol.getLocation());
                if (symbolNode != null) {
                    if (neighbors.contains(symbolNode)) {
                        return true;
                    }
                    if (node2.getNeighbors().contains(symbolNode)) {
                        return true;
                    }
                }
            }
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
        nodes.remove(node2);
        return node1.getClustered();
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

}
