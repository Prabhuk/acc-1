package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class AssignmentParser extends Parser {
    public AssignmentParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        final Token symbolName = tokenizer.next();
        if (!symbolName.isDesignator()) {
            throw new SyntaxErrorException("Designator expected. Found[" + symbolName.getToken() + "] instead");
        }
        final Token assignmentOp = tokenizer.next();
        if (!assignmentOp.isAssignmentOperator()) {
            throw new SyntaxErrorException("Assignment operator [<-] expected. Found[" + assignmentOp.getToken() + "] instead");
        }
        //$TODO Pending implementation. How do you generate code for assignment? How do you link it to PHI
        Result x = new Expression(code, tokenizer).parse();
        final boolean added = AuxiliaryFunctions.assignToSymbol(code, symbolName.getToken(), x, getSymbolTable());
        if (!added) {
            //$TODO$ handle registers
//            throw new RuntimeException("The Assignment value have to be either constant or variable. Found [" + x.kind().name() + "] instead");
        }

        return x;
    }


}
