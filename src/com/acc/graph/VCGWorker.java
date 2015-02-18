package com.acc.graph;

import com.acc.structure.BasicBlock;

/**
 * Created by Rumpy on 14-02-2015.
 */
public class VCGWorker implements Worker {
    int targetNameCounter=0;


    @Override
    public void setVisitationStatus(BasicBlock bb) {
        bb.countVisit();
    }

    @Override
    public void visit(BasicBlock source, BasicBlock destination) {
        int sourcename, destinationname;
        sourcename=source.getLabel();
        destinationname=destination.getLabel();

        print("edge: { sourcename: \""+sourcename+"\"\n" +
                "targetname: \""+destinationname+"\"\n" +
                "}");
        if(destination.visitationCounter()==1)
        {
            destination.countVisit();
            print("node: {" +
                    "title: \"" + destination.getLabel() + "\"\n" +
                    "label: \"" + destination.getLabel() + "[\n" +
                    "INSTRUCTION SET ]\"\n" +
                    "}");
        }


    }

    @Override
    public void printRootStatement(BasicBlock root) {
        print("graph: { title: \"Control Flow Graph\"\n" +
                "layoutalgorithm: dfs\n" +
                "manhattan_edges: yes\n" +
                "smanhattan_edges: yes\n" +
                "node: {" +
                "title: \"" + root.getLabel() + "\"\n" +
                "label: \"" + root.getLabel() + "[\n" +
                "INSTRUCTION SET ]\"\n" +
                "}");
    }

    private void print(String text) {
        System.out.println(text);
    }
}
