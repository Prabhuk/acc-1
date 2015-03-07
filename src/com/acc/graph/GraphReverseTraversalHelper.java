package com.acc.graph;

import com.acc.structure.BasicBlock;

import java.util.Set;

/**
 * Created by prabhuk on 3/4/2015.
 */
public class GraphReverseTraversalHelper extends GraphHelper {

    public GraphReverseTraversalHelper(Worker worker, BasicBlock lastNode) {
        super(worker, lastNode);
    }

    public GraphReverseTraversalHelper(Worker worker, BasicBlock root, BasicBlock end) {
        super(worker, root, end);
    }

    @Override
    protected Set<BasicBlock> getChildren(BasicBlock node) {
        return node.getParents();
    }
}
