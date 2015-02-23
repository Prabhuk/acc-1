package com.acc.ui;

import com.acc.data.*;
import com.acc.graph.GraphHelper;
import com.acc.graph.VCGWorker;
import com.acc.parser.Computation;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
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
            SSACode ssaCode = new SSACode();
            parser = new Computation(code, tokenizer);
            parser.parse();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Input file [" + filePath + "] not found");
            e.printStackTrace();
        }
    }

    public Computation getParser() {
        return parser;
    }

    public static void main(String[] args) {
        final Computation parser = new CompileInputFile("C:\\work\\acc\\test\\accsimple.txt").getParser();
        final Code code = parser.getCode();
        final List<Instruction> instructions = code.getInstructions();
        if (instructions.size() > 0) {
            System.out.println("Code:\n");
        }
        for (Instruction instruction : instructions) {
            System.out.println(instruction.getLocation() + "  "+ instruction.getInstructionString());
        }





        final SymbolTable symbolTable = parser.getSymbolTable();
        final List<Symbol> symbols = symbolTable.getSymbols();
        if (symbols.size() > 0) {
            System.out.println("\n\nGlobal Symbol Table: \n");
        }
        for (Symbol symbol : symbols) {
            Object value = symbol.getValue();
            if (symbol.getType().isArray()) {
                value = "Array dimensions: " + symbol.getArrayDimension();
            }
            System.out.println(symbol.getUniqueIdentifier() + " : " + value);
        }
        new GraphHelper(new VCGWorker(), code.getControlFlowGraph().getRootBlock());


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
