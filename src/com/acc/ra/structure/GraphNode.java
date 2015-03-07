package com.acc.ra.structure;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by prabhuk on 3/5/2015.
 */
public class GraphNode
//        implements Comparable<GraphNode>
{
    private int nodeId;
    Set<GraphNode> neighbors = new HashSet<GraphNode>();

    public GraphNode(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }


    public int getNodeCost() {
        return neighbors.size();
    }

    public Set<GraphNode> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<GraphNode> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(GraphNode neighbor) {
        neighbors.add(neighbor);
    }

    @Override
    public String toString() {
        return "GraphNode: " + String.valueOf(nodeId);
    }


//    @Override
//    public int compareTo(GraphNode o) {
//        return this.neighbors.size() - o.neighbors.size();
//    }
}
