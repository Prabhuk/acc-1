package com.acc.graph;

import com.acc.structure.BasicBlock;

/**
 * Created by Rumpy on 14-02-2015.
 */
public interface Worker {
    public void begin();
    public void visit(BasicBlock node);
    public void finish();
}
