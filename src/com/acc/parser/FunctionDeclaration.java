package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class FunctionDeclaration extends Parser {

    public FunctionDeclaration(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        return null;
    }
}
