package com.acc.ra.structure;

import com.acc.graph.Worker;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 3/7/2015.
 */
public class InterferenceGraphWorker extends Worker {

    private GraphNode root;
    InterferenceGraph graph = new InterferenceGraph();

    public InterferenceGraphWorker(Parser parser) {
        super(parser);
    }

    @Override
    public void visit(BasicBlock node) {
        final Set<Integer> liveRanges = node.getLiveRanges();
        if(liveRanges.isEmpty()) {
            return;
        }

        List<GraphNode> nodes = new ArrayList<GraphNode>();
        for (Integer liveRange : liveRanges) {
            final GraphNode gNode = new GraphNode(liveRange);
            nodes.add(gNode);
            graph.addNode(gNode);
        }
        for (GraphNode node1 : nodes) {
            for (GraphNode node2 : nodes) {
                if(!node1.equals(node2)) {
                    node1.addNeighbor(node2);
                }
            }
        }

        if(root == null) {
            graph.setRootNode(nodes.get(0));
        }
    }

    public GraphNode getRoot() {
        return root;
    }

    public InterferenceGraph getGraph() {
        return graph;
    }
}
