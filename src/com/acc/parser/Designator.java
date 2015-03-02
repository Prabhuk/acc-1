package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.structure.SymbolTable;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class Designator extends Parser {

    public Designator(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
//        Result x;
//        x = new Ident(code, tokenizer, symbolTable).parse(); $TODO$ Nik - This should be handled at the Tokenizer
        return null;
    }
}
