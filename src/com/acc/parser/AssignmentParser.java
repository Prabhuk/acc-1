package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.SSACode;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.util.Tokenizer;

import java.util.List;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class AssignmentParser extends Parser {
    public AssignmentParser(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        final Token symbolName = tokenizer.next();

        if (!symbolName.isDesignator()) {
            throw new SyntaxErrorException("Designator expected. Found[" + symbolName.getToken() + "] instead");
        }
        final Symbol recent = getSymbolTable().getRecentOccurence(symbolName.getToken());
        List<Result> arrayIdentifiers = accumulateArrayIdentifiers(recent);
        final Token assignmentOp = tokenizer.next();
        if (!assignmentOp.isAssignmentOperator()) {
            throw new SyntaxErrorException("Assignment operator [<-] expected. Found[" + assignmentOp.getToken() + "] instead");
        }
        Result x = new Expression(code, tokenizer, ssaCode).parse();
        assignSymbol(symbolName.getToken(), recent, x, arrayIdentifiers);
        return x;
    }






}
