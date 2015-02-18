package com.acc.graph;

import com.acc.structure.BasicBlock;

/**
 * Created by Rumpy on 14-02-2015.
 */
public interface Worker {
    public void setVisitationStatus(BasicBlock bb);
    public void visit(BasicBlock source, BasicBlock destination);

    void printRootStatement(BasicBlock root);
}
