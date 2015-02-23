package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.SSACode;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 02-02-2015.
 */
public class StatSequence extends Parser {
    public StatSequence(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        Result x = new Statement(code, tokenizer, ssaCode).parse();
        while (tokenizer.next().isSemicolon()) {
            Result y = new Statement(code, tokenizer, ssaCode).parse();
            if (y.getJoin() != null) {
                if (x.getJoin() != null) {
                    System.out.println("Seems to be Join block clashing"); //$TODO$ for debugging. Needs to be removed
                }
                x.setJoin(y.getJoin());
            }
        }
        tokenizer.previous(); // moving a step back from the one who is not a semicolon
        return x; //Necessary to return the results at this level? what is the purpose at this level?
    }
}
