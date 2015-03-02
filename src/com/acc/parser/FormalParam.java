package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.SymbolTable;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 12-02-2015.
 */
public class FormalParam extends Parser {

    public FormalParam(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        Token next = tokenizer.next();
        if (next.getToken().equals(",")) {
            next = tokenizer.next();
        }
        Result x = new Result(Kind.VAR);
        x.setVariableName(next.getToken());
        return x;
    }


}
