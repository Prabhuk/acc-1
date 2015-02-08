package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.util.Tokenizer;


/**
 * Created by prabhuk on 1/26/2015.
 */
public class IfParser extends Parser{
    public IfParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = null;
        //$TODO Pending implementation
        if(tokenizer.next().getToken() == "if")
        {
            x = new Relation(code,tokenizer).parse();
            if(tokenizer.next().getToken() == "then")
            {
                x = new StatSequence(code,tokenizer).parse();
            }
            else
            {
                return null; //ERROR!!! IF should always be followed by a THEN
            }
            while(tokenizer.current().getToken()=="else")
            {
                tokenizer.next();
                x = new StatSequence(code,tokenizer).parse();
            }
            if(tokenizer.next().getToken() == "fi")
                return x;
            else
                return null;  //Error if condition has to end with a fi
        }
        return null;
    }
}
