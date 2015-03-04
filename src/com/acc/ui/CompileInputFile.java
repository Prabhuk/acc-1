package com.acc.ui;

import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.graph.*;
import com.acc.parser.Computation;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.util.Printer;
import com.acc.util.Tokenizer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prabhuk on 1/14/2015.
 */
public class CompileInputFile {
    private Tokenizer tokenizer;
    public static String currentFileName;
    Computation parser;
    private static Logger logger = Logger.getLogger(CompileInputFile.class.getName());

    public CompileInputFile(String filePath, OutputContents contents) {
        try {
            tokenizer = new Tokenizer(filePath);
            Code code = new Code();
            parser = new Computation(code, tokenizer, new SymbolTable(), "main", contents);
            parser.parse();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Input file [" + filePath + "] not found");
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {


        final Collection<File> files = FileUtils.listFiles(new File("C:\\work\\acc\\test"), new String[]{"txt"}, false);
//        final Collection<File> files = FileUtils.listFiles(new File("C:\\work\\acc\\test2"), new String[]{"txt"}, false);
        for (File inputFile : files) {
            currentFileName = inputFile.getName();
            processFile(inputFile.getAbsolutePath(), inputFile.getName());
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

    private static void processFile(String inputFile, String name) {
        final String prefix = name.substring(0, name.indexOf("."));
        final OutputContents contents = new OutputContents();
        new CompileInputFile(inputFile, contents);
        final List<Computation> parsers = contents.getPrograms();

        final Computation mainProgram = contents.getMainProgram();

        for (Computation parser : parsers) {

            final Code code = parser.getCode();
            printInstructions(parser, code);
            final BasicBlock rootNode = code.getControlFlowGraph().getRootBlock();
            new GraphHelper(new CPWorker(parser), rootNode);
            new GraphHelper(new DeleteInstructions(code, parser), rootNode);
            printInstructions(parser, code);
            new GraphHelper(new CSEWorker(parser), rootNode);
            new GraphHelper(new DeleteInstructions(code, parser), rootNode);
            final DCEWorker worker = new DCEWorker(code);
            worker.visit();
            printInstructions(parser, code);
            new GraphHelper(new VCGWorker("output\\" + prefix+"_" + parser.getProgramName() + ".vcg", parser), rootNode);
        }
        Printer.print("Compilation completed for ["+inputFile+"]");
    }

    private static void printInstructions(Computation parser, Code code) {
        final List<Instruction> instructions = code.getInstructions();
        if (instructions.size() > 0) {
            System.out.println("Code for:["+parser.getProgramName()+"] \n");
        }
        for (Instruction instruction : instructions) {
            System.out.println(instruction.getLocation() + "  "+ instruction.getInstructionString());
        }

        final SymbolTable symbolTable = parser.getSymbolTable();
        final List<Symbol> symbols = symbolTable.getSymbols();
        for (Symbol symbol : symbols) {
            Printer.print(symbol.getName() + " " + symbol.getSuffix());
        }
    }

}
