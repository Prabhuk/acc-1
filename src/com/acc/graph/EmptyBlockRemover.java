package com.acc.graph;

import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by prabhuk on 3/10/2015.
 */
public class EmptyBlockRemover extends Worker {
    Set<BasicBlock> nodes = new HashSet<BasicBlock>();

    public EmptyBlockRemover(Parser parser) {
        super(parser);
    }

    @Override
    public void visit(BasicBlock node) {
        if(node.getInstructions().isEmpty()) {
            nodes.add(node);
        }
    }

    @Override
    public void finish() {
        for (BasicBlock node : nodes) {
            node.setDeleted(true);
            final Set<BasicBlock> children = node.getChildren();
            final Set<BasicBlock> parents = node.getParents();
            BasicBlock selectedParent1 = null;
            BasicBlock selectedParent2 = null;
            for (BasicBlock parent : parents) {
                if(selectedParent1 == null) {
                    selectedParent1 = parent;
                } else {
                    selectedParent2 = parent;
                }
            }
            for (BasicBlock child : children) {
                if(child.getLeft().equals(node)) {
                    child.setLeft(selectedParent1);
                }
            }
            for (BasicBlock parent : parents) {
                parent.getChildren().remove(node);
                if(parent.getLeft()!= null && parent.getLeft().equals(node)) {
                    for (BasicBlock child : children) {
                        parent.setLeft(child);
                    }
                }
                if(parent.getRight()!= null && parent.getRight().equals(node)) {
                    for (BasicBlock child : children) {
                        parent.setRight(child);
                    }
                }
                for (BasicBlock child : children) {
                    parent.addChild(child);
                }
            }
        }
    }
}
