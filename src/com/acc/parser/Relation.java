package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.RelationalOperator;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rumpy on 04-02-2015.
 */
public class Relation extends Parser {

    public Relation(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        Result x = new Expression(code, tokenizer, symbolTable).parse();
        List<Result> arrayIdentifiers = new ArrayList<Result>();
        if(x.kind().isArray()) {
            arrayIdentifiers = x.getArrayIdentifiers();
            //$TODO$ what to do with it?
        }
        Token next = tokenizer.next();
        x.setCondition(new RelationalOperator(next.getToken()).value());
        Result y = new Expression(code, tokenizer, symbolTable).parse();
        List<Result> arrayIdentifiers2 = new ArrayList<Result>();
        if(y.kind().isArray()) {
            arrayIdentifiers2 = y.getArrayIdentifiers();
            //$TODO$ what to do with it?
        }
        AuxiliaryFunctions.addInstruction(OperationCode.cmp, code, x, y, getSymbolTable());
        return x;
    }
}
