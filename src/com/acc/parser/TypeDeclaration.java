package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.*;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class TypeDeclaration extends Parser {

    public TypeDeclaration(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Kind kind;
        Integer address = null;
        Token next = tokenizer.next();
        List<Integer> dimensions = null;
        if (next.isKeyword() && ((Keyword) next).isVar()) {
            kind = Kind.VAR;
            //$TODO$ set address
        } else if (next.isKeyword() && ((Keyword) next).isArray()) {
            kind = Kind.ARRAY;
            dimensions = new ArrayList<Integer>();
            next = tokenizer.next();
            while (next.getToken().equals("[")) {
                next = tokenizer.next();
                if (!next.isConstant()) {
                    throw new SyntaxErrorException("Number expected for array dimension declaration. Found [" + next.getToken() + "] instead");
                }
                dimensions.add(Integer.parseInt(next.getToken()));
                next = tokenizer.next();
                if (!isClosedBracket(next)) {
                    throw new SyntaxErrorException("Symbol \"]\" expected for array declaration. Found [" + next.getToken() + "] instead");
                }
                next = tokenizer.next();
            }
            if(dimensions.size() == 0) {
                throw new SyntaxErrorException("Symbol \"[\" expected for array declaration. Found [" + next.getToken() + "] instead");
            }
            tokenizer.previous(); // fixing the tokenizer to refer back to the previous variable
        } else {
            throw new SyntaxErrorException("Keyword \"var\" or \"array\" expected for type declaration. Found [" + next.getToken() + "] instead");
        }
        return new Result(kind, null, null, address, null, null, dimensions);
    }

    private boolean isClosedBracket(Token next) {
        return next.getToken().equals("]");
    }

}
