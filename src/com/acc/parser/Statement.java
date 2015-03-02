package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.structure.SymbolTable;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class Statement extends Parser {

    public Statement(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        Result x = null;
        final Token next = tokenizer.next();
        if (next.isKeyword()) {
            Keyword nextKeyword = (Keyword) next;
            if (nextKeyword.isStatementBeginKeyword()) {
                if (nextKeyword.isLet()) {
                    x = new AssignmentParser(code, tokenizer, symbolTable).parse();
                } else if (nextKeyword.isCall()) {
                    x = new FunctionCall(code, tokenizer, symbolTable).parse();
                } else if (nextKeyword.isWhile()) {
                    x = new WhileParser(code, tokenizer, symbolTable).parse();
                } else if (nextKeyword.isReturn()) {
                    x = new ReturnParser(code, tokenizer, symbolTable).parse();
                } else if (nextKeyword.isIf()) {
                    x = new IfParser(code, tokenizer, symbolTable).parse();
                }
            }
//            tokenizer.previous(); // Statements breakout on semicolon or end curly brace.
        } else {
            tokenizer.previous(); //moving back to reread the symbolName
            x = new AssignmentParser(code, tokenizer, symbolTable).parse();
        }
        // Rolling back to those tokens for statSequence to stay sane.
        return x;
    }


}
