package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class VarDeclr extends Parser{

    public VarDeclr(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x;
        x= new TypeDeclr(code,tokenizer).parse();
        if(x==null)
            return null; //ERROR! Type Declr must be present
        do
        {x=new ident(code, tokenizer).parse();}while(tokenizer.current())

        return null;
    }
}
