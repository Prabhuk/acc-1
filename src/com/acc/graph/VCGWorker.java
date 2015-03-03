package com.acc.graph;

import com.acc.data.Instruction;
import com.acc.parser.Parser;
import com.acc.structure.BasicBlock;
import com.acc.structure.SymbolTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Rumpy on 14-02-2015.
 */
public class VCGWorker extends Worker {

    private final String outputFileName;
    private BufferedWriter bufferedWriter;

    public VCGWorker(String outputFileName, Parser parser) {
        super(parser);
        this.outputFileName = outputFileName;
    }

    @Override
    public void begin() {
        String fileName = outputFileName;
        final File file = new File(fileName);
        System.out.println(file.getAbsolutePath());
        try {
            FileWriter out = new FileWriter(file);
            bufferedWriter = new BufferedWriter(out);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                bufferedWriter.close();
            } catch (IOException e1) {
                //ignore
            }
        }
        print("graph: {\n" +
                "x: 150\n" +
                "y: 20\n" +
                "xmax: 960\n" +
                "ymax: 900\n" +
                "width: 950\n" +
                "height: 900\n" +
                "layoutdownfactor: 100\n" +
                "layoutupfactor: 0\n" +
                "layoutnearfactor: 0\n" +
                "yspace: 30\n" +
                "smanhattenedges: yes\n" +
                "fasticons: yes\n" +
                "iconcolors: 32\n");
    }

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

    @Override
    public void finish() {
        super.finish();
        print("}");
        if(bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                //ignore
            }
        }
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
            instructionString.append(instruction.getLocation()).append(" ").append(instruction.getInstructionString()).append("\n");
        }
        return instructionString;
    }

    private void print(String text) {
        if(bufferedWriter != null) {
            try {
                bufferedWriter.write(text);
            } catch (IOException e) {
                e.printStackTrace();
                if(bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e1) {
                        //ignore
                    }
                }
            }
        }
//        System.out.println(text);
    }
}
