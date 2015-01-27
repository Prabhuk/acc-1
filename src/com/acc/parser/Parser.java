package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public abstract class Parser {
    protected Code code;
    protected Tokenizer tokenizer;

    public Parser(Code code, Tokenizer tokenizer) {
        this.code = code;
        this.tokenizer = tokenizer;
    }

    public abstract Result parse();
}
