package com.acc.graph;

import com.acc.structure.BasicBlock;

/**
 * Created by Rumpy on 14-02-2015.
 */
public class GraphHelper {
    private Worker worker;

    public GraphHelper(Worker worker, BasicBlock root)
    {
        this.worker = worker;
        worker.begin();
        processGraph(root);
        worker.finish();
        worker.unMarkVisited();
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

        for(BasicBlock n: node.getChildren())
        {
            if(!n.isVisited()) {
                processGraph(n);
            }
        }
    }
}
