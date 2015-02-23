package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.util.Printer;
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
            Result y = new Statement(code, tokenizer).parse();
            if (y.getJoin() != null) {
                if (x.getJoin() != null) {
                    Printer.debugMessage("Seems to be Join block clashing");
                }
                x.setJoin(y.getJoin());
            }
        }
        tokenizer.previous(); // moving a step back from the one who is not a semicolon
        return x; //Necessary to return the results at this level? what is the purpose at this level?
    }
}
