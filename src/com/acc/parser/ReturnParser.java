package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.SSACode;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class ReturnParser extends Parser {

    public ReturnParser(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        //$TODO Pending implementation
        return null;
    }
}
