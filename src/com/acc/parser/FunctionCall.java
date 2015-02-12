package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.data.TokenType;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/8/2015.
 */
public class FunctionCall extends Parser {

    public FunctionCall(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        final Token procedureName = tokenizer.next();
        if(!procedureName.isIdentifier()) {
            throw new SyntaxErrorException(procedureName.tokenType(), TokenType.IDENTIFIER);
        }
        final Token openBracket = tokenizer.next();
        if(!openBracket.getToken().equals("(")) {
            throw new SyntaxErrorException("Excpected \"(\" after procedure name ["+procedureName.getToken()+"]. Found ["+openBracket.getToken()+"] instead");
        }
        Token lookAhead = tokenizer.next();
        List<Result> parameters = new ArrayList<Result>();

        if(!lookAhead.getToken().equals(")")) {
            tokenizer.previous(); //Allowing expression to process the first parameter value
            parameters.add(new Expression(code, tokenizer).parse());
            lookAhead = tokenizer.next();
            while (lookAhead.getToken().equals(",")) {
                parameters.add(new Expression(code, tokenizer).parse());
            }
            if(!lookAhead.getToken().equals(")")) { //End brackets should've broken the while loop. Otherwise syntax error
                throw new SyntaxErrorException("Expected [\")\"]. Found ["+lookAhead.getToken()+"] instead");
            }
        }
        //$TODO$ Generate code to make the procedure call with parameters list
        return null; //$TODO$ return relevant value
    }
}
