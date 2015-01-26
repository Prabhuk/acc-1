package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Term {

    private Code code;
    private Tokenizer tokenizer;

    public Term(Code code, Tokenizer tokenizer) {
        this.code = code;
    }

    public Result parse() {
        //$TODO$ Pending implementation
        Result x = null;
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();

        }
        return x;
    }
}
