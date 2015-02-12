package com.acc.parser;

import com.acc.constants.KeywordType;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class WhileParser extends Parser {
    public WhileParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);

    }

    @Override
    public Result parse() {
        final int loop = code.getPc();
        Result x = new Relation(code, tokenizer).parse();
        AuxiliaryFunctions.CJF(code, x);
        final Token next = tokenizer.next();
        if (!next.isKeyword() || !((Keyword) next).isDo()) {
            throw new SyntaxErrorException(KeywordType.DO, next);
        }
        new StatSequence(code, tokenizer).parse();
        AuxiliaryFunctions.BJ(code, loop); //Backward Jump to the loop beginning.
        if (!next.isKeyword() || !((Keyword) next).isOd()) {
            throw new SyntaxErrorException(KeywordType.OD, next);
        }

        return x; //$TODO$ Do we need x at all?
    }
}
