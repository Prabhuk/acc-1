package com.acc.parser;

import com.acc.data.*;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class Statement extends Parser {

    public Statement(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        Result x = null;
        final Token next = tokenizer.next();
        if (next.isKeyword()) {
            Keyword nextKeyword = (Keyword) next;
            if (nextKeyword.isStatementBeginKeyword()) {
                if (nextKeyword.isLet()) {
                    x = new AssignmentParser(code, tokenizer, ssaCode).parse();
                } else if (nextKeyword.isCall()) {
                    x = new CallParser(code, tokenizer, ssaCode).parse();
                } else if (nextKeyword.isWhile()) {
                    x = new WhileParser(code, tokenizer, ssaCode).parse();
                } else if (nextKeyword.isReturn()) {
                    x = new ReturnParser(code, tokenizer, ssaCode).parse();
                } else if (nextKeyword.isIf()) {
                    x = new IfParser(code, tokenizer, ssaCode).parse();
                }
            }
//            tokenizer.previous(); // Statements breakout on semicolon or end curly brace.
        } else {
            tokenizer.previous(); //moving back to reread the symbolName
            x = new AssignmentParser(code, tokenizer, ssaCode).parse();
        }
        // Rolling back to those tokens for statSequence to stay sane.
        return x;
    }


}
