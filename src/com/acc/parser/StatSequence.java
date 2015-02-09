package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 02-02-2015.
 */
public class StatSequence extends Parser {
    public StatSequence(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = new Statement(code, tokenizer).parse();
        while (tokenizer.next().isSemicolon()) {
            x = new Statement(code, tokenizer).parse();
        }
        return x; //Necessary to return the results at this level? what is the purpose at this level?
    }
}
