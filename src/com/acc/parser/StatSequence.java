package com.acc.parser;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 02-02-2015.
 */
public class StatSequence extends Parser{
    public StatSequence(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x=null;
        do
        {
            if(tokenizer.current().getToken().equals(";"))
                tokenizer.next();
            x=new Statement(code, tokenizer).parse();
        }while(tokenizer.current().getToken().equals(";"));
        return x;
    }
}
