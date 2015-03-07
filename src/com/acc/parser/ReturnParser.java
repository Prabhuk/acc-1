package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.structure.SymbolTable;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class ReturnParser extends Parser {

    public ReturnParser(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        //$TODO Pending implementation
        final Result x = new Expression(code, tokenizer, symbolTable).parse();
        //$TODO$ watch out for returning array cases
        AuxiliaryFunctions.addInstruction(OperationCode.ret, code, x, null, symbolTable);// $TODO$ x is intermediate value - move it to return register R31
        return x;
    }
}
