package com.acc.graph;

import com.acc.data.Instruction;
import com.acc.structure.BasicBlock;

import java.util.List;
import java.util.Set;

/**
 * Created by Rumpy on 14-02-2015.
 */
public class VCGWorker implements Worker {

    @Override
    public void visit(BasicBlock node) {
        final String name = String.valueOf(node.getLabel());
        StringBuilder instructionString = getInstructionString(node);

        final Set<BasicBlock> children = node.getChildren();
        for (BasicBlock child : children) {
            String destinationName = String.valueOf(child.getLabel());
            print(getEdge(name, destinationName));
        }
        print(getNodeString(node, instructionString));
    }

    private String getNodeString(BasicBlock node, StringBuilder instructionString) {
        StringBuilder sb = new StringBuilder();
        sb.append("node: {title: \"").append(node.getLabel()).append("\"\n")
                .append("label: \"").append(node.getLabel()).append("[\n")
                .append("").append(instructionString.toString()).append(" ]\"\n").append("}");
        return sb.toString();
    }

    private String getEdge(String name, String destinationName) {
        StringBuilder sb = new StringBuilder();
        sb.append("edge: { sourcename: \"").append(name).append("\"\n")
                .append("targetname: \"").append(destinationName).append("\"\n").append("}");
        return sb.toString();
    }

    private StringBuilder getInstructionString(BasicBlock destination) {
        StringBuilder instructionString = new StringBuilder();
        final List<Instruction> instructions = destination.getInstructions();
        for (Instruction instruction : instructions) {
            instructionString.append(instruction.getInstructionString()).append("\n");
        }
        return instructionString;
    }

    private void print(String text) {
        System.out.println(text);
    }
}
