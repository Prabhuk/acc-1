package com.acc.graph;

import com.acc.structure.BasicBlock;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rumpy on 14-02-2015.
 */
public abstract class Worker {
    Set<BasicBlock> allNodes = new HashSet<BasicBlock>();
    public void begin() {}

    public void visit(BasicBlock node) {
           allNodes.add(node);
    }

    public void finish() {
        for (BasicBlock allNode : allNodes) {
            allNode.setVisited(false);
        }

    }
}
