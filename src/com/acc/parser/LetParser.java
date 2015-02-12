package com.acc.parser;

import com.acc.data.*;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class LetParser extends Parser {
    public LetParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        final Token varialbleName = tokenizer.next();
        if (!varialbleName.isDesignator()) {
            throw new SyntaxErrorException("Designator expected. Found[" + varialbleName.getToken() + "] instead");
        }
        final Token assignmentOp = tokenizer.next();
        if (!assignmentOp.isAssignmentOperator()) {
            throw new SyntaxErrorException("Assignment operator [<-] expected. Found[" + assignmentOp.getToken() + "] instead");
        }

        //$TODO Pending implementation. How do you generate code for assignment? How do you link it to PHI

//        AuxiliaryFunctions.putF1();

        Result x = new Expression(code, tokenizer).parse();
        //$TODO x to be assigned to variableName

        return x;
    }
}
