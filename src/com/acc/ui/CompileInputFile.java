package com.acc.ui;

import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.graph.CopyPropogationWorker;
import com.acc.graph.DeleteInstructions;
import com.acc.graph.GraphHelper;
import com.acc.graph.VCGWorker;
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
    private String filePath;
    private Tokenizer tokenizer;
    public static String currentFileName;
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


        final String inputFile = "C:\\work\\acc\\test\\accsimple.txt";
        currentFileName = "accsimple.txt";
        processFile(inputFile);
//        final Collection<File> files = FileUtils.listFiles(new File("C:\\work\\acc\\test"), new String[]{"txt"}, false);
//        for (File inputFile : files) {
//            currentFileName = inputFile.getName();
//            processFile(inputFile.getAbsolutePath());
//        }


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

    private static void processFile(String inputFile) {
        new CompileInputFile(inputFile);
        final List<Computation> parsers = OutputContents.getPrograms();
        for (Computation parser : parsers) {
            final Code code = parser.getCode();
            printInstructions(parser, code);
            final BasicBlock rootNode = code.getControlFlowGraph().getRootBlock();
            new GraphHelper(new CopyPropogationWorker(parser.getSymbolTable()), rootNode);
            new GraphHelper(new DeleteInstructions(code, parser.getSymbolTable()), rootNode);
            new GraphHelper(new VCGWorker(parser.getProgramName() + ".vcg", parser.getSymbolTable()), rootNode);
            printInstructions(parser, code);
        }
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
