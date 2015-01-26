package com.acc.parser;

import com.acc.data.*;
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

    public void parse() {
       while (tokenizer.hasNext()) {
           final Token next = tokenizer.next();

       }
    }
}
