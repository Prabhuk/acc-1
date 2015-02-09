package com.acc.structure;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 2/8/2015.
 */
public class DominatorTreeNode {
    List<BasicBlock> children = new LinkedList<BasicBlock>();
    BasicBlock parent = null;

    public DominatorTreeNode() {
    }

    public void setParent(BasicBlock parent) {
        this.parent = parent;
    }
}
