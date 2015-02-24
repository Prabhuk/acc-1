package com.acc.graph;

import com.acc.structure.BasicBlock;
import com.acc.structure.SymbolTable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rumpy on 14-02-2015.
 */
public abstract class Worker {
    protected final SymbolTable symbolTable;
    Set<BasicBlock> allNodes = new HashSet<BasicBlock>();

    public Worker(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void begin() {}

    public void visit(BasicBlock node) {
           allNodes.add(node);
    }

    public void finish() {
        for (BasicBlock allNode : allNodes) {
            allNode.setVisited(false);
        }

    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}
