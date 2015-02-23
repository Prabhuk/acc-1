package com.acc.ui;

import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.graph.GraphHelper;
import com.acc.graph.VCGWorker;
import com.acc.parser.Computation;
import com.acc.util.Tokenizer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prabhuk on 1/14/2015.
 */
public class CompileInputFile {
    private String filePath;
    private Tokenizer tokenizer;
    Computation parser;
    private static Logger logger = Logger.getLogger(CompileInputFile.class.getName());

    public CompileInputFile(String filePath) {
        this.filePath = filePath;
        try {
            tokenizer = new Tokenizer(filePath);
            Code code = new Code();
            parser = new Computation(code, tokenizer, "main");
            parser.parse();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Input file [" + filePath + "] not found");
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {


        new CompileInputFile("C:\\work\\acc\\test\\accsimple.txt");

        final List<Computation> parsers = OutputContents.getPrograms();
        for (Computation parser : parsers) {
            final Code code = parser.getCode();
            final List<Instruction> instructions = code.getInstructions();
            if (instructions.size() > 0) {
                System.out.println("Code:\n");
            }
            for (Instruction instruction : instructions) {
                System.out.println(instruction.getLocation() + "  "+ instruction.getInstructionString());
            }
            new GraphHelper(new VCGWorker(parser.getProgramName() + ".vcg"), code.getControlFlowGraph().getRootBlock());
        }


//        BasicBlock bb0= new BasicBlock();
//        BasicBlock bb1= new BasicBlock();
//        BasicBlock bb2= new BasicBlock();
//        BasicBlock bb3= new BasicBlock();
//
//        bb0.addChild(bb1);
//        bb1.addChild(bb2);
//        bb1.addChild(bb3);
//        bb2.addChild(bb1);
//        GraphHelper v = new GraphHelper(new VCGWorker(), bb0);
    }

}
