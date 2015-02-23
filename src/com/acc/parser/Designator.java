package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.SSACode;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class Designator extends Parser {

    public Designator(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
//        Result x;
//        x = new Ident(code, tokenizer).parse(); $TODO$ Nik - This should be handled at the Tokenizer
        return null;
    }
}
