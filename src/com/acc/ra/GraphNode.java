package com.acc.ra;

import com.acc.data.Instruction;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by prabhuk on 3/5/2015.
 */
public class GraphNode
{
    private int nodeId;
    private Set<GraphNode> neighbors = new HashSet<GraphNode>();
    private Set<GraphNode> clustered = new HashSet<GraphNode>();
    private Set<Instruction> moveInstructions = new HashSet<Instruction>();

    public GraphNode(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }


    public Set<GraphNode> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(GraphNode neighbor) {
        neighbors.add(neighbor);
    }

    @Override
    public String toString() {
        return "GraphNode: " + String.valueOf(nodeId);
    }

    public void addToCluster(GraphNode node) {
        clustered.add(node);
    }

    public Set<GraphNode> getClustered() {
        return clustered;
    }

    public void addToMoveInstructions(Instruction instruction) {
        moveInstructions.add(instruction);
    }

    public Set<Instruction> getMoveInstructions() {
        return moveInstructions;
    }
}
