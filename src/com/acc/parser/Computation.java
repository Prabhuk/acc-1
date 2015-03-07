package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.SymbolTable;
import com.acc.ui.OutputContents;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.List;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class Computation extends Parser {

    private String programName;
    private List<String> formalParams;

    public Computation(Code code, Tokenizer tokenizer, SymbolTable symbolTable, String programName, OutputContents outputContents) {
        super(code, tokenizer, symbolTable);
        this.programName = programName;
        this.setOutputContents(outputContents);
        System.out.println("CURRENT PROGRAM: " + this.programName);
        this.outputContents.addProgram(this);
    }

    @Override
    public Result parse() {
        Token next = tokenizer.next();
        if (!next.getToken().equals("main")) {
            throw new SyntaxErrorException("Your program must begin with keyword [main]");
        }
        final Result s = programBody();
        Token end = tokenizer.next();
        if (!end.getToken().equals(".")) {
            throw new SyntaxErrorException("Your program should end with a dot [.] Found [" + end + "] instead");
        }
        AuxiliaryFunctions.addInstruction(OperationCode.end, code, null, null, getSymbolTable());
        return s;
    }

    public Result programBody() {

        new VariableDeclaration(code, tokenizer, symbolTable).parse();

        Token next = tokenizer.next();
        if (!next.getToken().equals("{")) {
            throw new SyntaxErrorException("Expected \"{\". Found [" + next + "] instead");
        }
        final Result s = new StatSequence(code, tokenizer, symbolTable).parse();

        next = tokenizer.next();
        if (!next.getToken().equals("}")) {
            throw new SyntaxErrorException("Expected \"}\". Found [" + next + "] instead");
        }
        code.setLastNode();
        return s;
    }

    public String getProgramName() {
        return programName;
    }

    public List<String> getFormalParams() {
        return formalParams;
    }

    public void setFormalParams(List<String> formalParams) {
        this.formalParams = formalParams;
    }
}
