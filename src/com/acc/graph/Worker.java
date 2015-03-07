package com.acc.graph;

import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;
import com.acc.structure.SymbolTable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rumpy on 14-02-2015.
 */
public abstract class Worker {
    protected final Parser parser;
    Set<BasicBlock> allNodes = new HashSet<BasicBlock>();

    public Worker(Parser parser) {
        this.parser = parser;
    }

    public void begin() {}

    public abstract void visit(BasicBlock node);

    public void addToNodes(BasicBlock node) {
        allNodes.add(node);
    }

    public void finish() {}

    public void unMarkVisited() {
        for (BasicBlock allNode : allNodes) {
            allNode.setVisited(false);
        }
    }

    public Set<BasicBlock> getAllNodes() {
        return allNodes;
    }

    public SymbolTable getSymbolTable() {
        return parser.getSymbolTable();
    }
}
