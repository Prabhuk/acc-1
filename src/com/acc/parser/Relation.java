package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 04-02-2015.
 */
public class Relation extends Parser{
    public Relation(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = null;
        x= new Expression(code,tokenizer).parse();
        if(x!=null)
            x= new Relation(code,tokenizer).parse();
        else
            return null;
        if (x!=null)
            x=new Expression(code,tokenizer).parse();
        else
            return null;
        return x;
    }
}
