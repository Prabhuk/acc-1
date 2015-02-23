package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.List;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class AssignmentParser extends Parser {
    public AssignmentParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        final Token lhs = tokenizer.next();

        if (!lhs.isDesignator()) {
            throw new SyntaxErrorException("Designator expected. Found[" + lhs.getToken() + "] instead");
        }
        final Symbol recent = getSymbolTable().getRecentOccurence(lhs.getToken());
        List<Result> arrayIdentifiers = accumulateArrayIdentifiers(recent);
        //$TODO$ well do something with the identifiers
        Result x = new Result(recent.getType().isArray() ? Kind.ARRAY : Kind.VAR, null, null, null, null, null, null);
        x.setVariableName(recent.getName());

        final Token assignmentOp = tokenizer.next();
        if (!assignmentOp.isAssignmentOperator()) {
            throw new SyntaxErrorException("Assignment operator [<-] expected. Found[" + assignmentOp.getToken() + "] instead");
        }

        Result y = new Expression(code, tokenizer).parse();

        AuxiliaryFunctions.addMoveInstruction(code, x, y, getSymbolTable());

        return y;
    }






}
