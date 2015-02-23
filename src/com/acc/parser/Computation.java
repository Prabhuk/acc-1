package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.ui.OutputContents;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class Computation extends Parser {

    private String programName;

    public Computation(Code code, Tokenizer tokenizer, String programName) {
        super(code, tokenizer);
        this.programName = programName;
        OutputContents.addProgram(this);
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
        return s;
    }

    public Result programBody() {

        new VariableDeclaration(code, tokenizer).parse();
        new FunctionDeclaration(code, tokenizer).parse();

        Token next = tokenizer.next();
        if (!next.getToken().equals("{")) {
            throw new SyntaxErrorException("Expected \"{\". Found [" + next + "] instead");
        }
        final Result s = new StatSequence(code, tokenizer).parse();

        next = tokenizer.next();
        if (!next.getToken().equals("}")) {
            throw new SyntaxErrorException("Expected \"}\". Found [" + next + "] instead");
        }
        return s;
    }

    public String getProgramName() {
        return programName;
    }
}
