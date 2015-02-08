package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class Statement extends Parser {

    public Statement(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = null;
            final Token next = tokenizer.next();
            if (next.tokenType().isKeyword()) {
                Keyword nextKeyword = (Keyword) next;
                if (nextKeyword.type().isStatementBeginKeyword()) {
                    if (nextKeyword.type().isLet()) {
                        x = new LetParser(code, tokenizer).parse();
                    } else if (nextKeyword.type().isCall()) {
                        x = new CallParser(code, tokenizer).parse();
                    } else if (nextKeyword.type().isWhile()) {
                        x = new WhileParser(code, tokenizer).parse();
                    } else if (nextKeyword.type().isReturn()) {
                        x = new ReturnParser(code, tokenizer).parse();
                    } else if (nextKeyword.type().isIf()) {
                        x = new IfParser(code, tokenizer).parse();
                    }
                }

        }
        return x;
    }


}
