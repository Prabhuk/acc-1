package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 04-02-2015.
 */
public class Relation extends Parser {

    public Relation(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = new Expression(code, tokenizer).parse();
        Token next = tokenizer.next();
        x.setCondition(new RelationalOperator(next.getToken()).value());
        Result y = new Expression(code, tokenizer).parse();
        AuxiliaryFunctions.addInstruction(OperationCode.cmp, code, x, y, getSymbolTable());
        return x;
    }
}
