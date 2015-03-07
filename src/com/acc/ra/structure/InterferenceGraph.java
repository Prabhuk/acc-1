package com.acc.ra.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 3/5/2015.
 */
public class InterferenceGraph {
    private Set<GraphNode> nodes = new HashSet<GraphNode>();
    private GraphNode rootNode;

    public Set<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(Set<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public GraphNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(GraphNode rootNode) {
        this.rootNode = rootNode;
    }

    public void addNode(GraphNode node) {
        nodes.add(node);
    }
}
