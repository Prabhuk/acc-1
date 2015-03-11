package com.acc.graph;

import com.acc.structure.BasicBlock;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rumpy on 14-02-2015.
 */
public class GraphHelper {
    protected Worker worker;
    Set<BasicBlock> secondaryVisit = new HashSet<BasicBlock>();

    public GraphHelper(Worker worker, BasicBlock root)
    {
        this.worker = worker;
        worker.begin();
        processGraph(root);
        worker.finish();
        worker.unMarkVisited();
    }

    public GraphHelper(Worker worker, BasicBlock root, BasicBlock end)
    {
        this.worker = worker;
        secondaryVisit = new HashSet<BasicBlock>();
        processSubGraph(root, end);
    }

    public void processGraph(BasicBlock node)
    {
        if (node == null) {
            return;
        }
        if(!node.isVisited()) {
            worker.visit(node);
            worker.addToNodes(node);
            node.setVisited(true);
        }

        for(BasicBlock n: getChildren(node))
        {
            if(!n.isVisited()) {
                processGraph(n);
            }
        }
    }

    public void processSubGraph(BasicBlock node, BasicBlock endNode)
    {
        if (node == null || node.equals(endNode)) {
            return;
        }
        if(!secondaryVisit.contains(node)) {
            secondaryVisit.add(node);
            worker.visit(node);
        }

        for(BasicBlock n: getChildren(node))
        {
            if(!secondaryVisit.contains(n)) {
                processSubGraph(n, endNode);
            }
        }
    }

    protected Set<BasicBlock> getChildren(BasicBlock node) {
        return node.getChildren();
    }
}
