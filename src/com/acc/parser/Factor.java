package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.*;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class Factor extends Parser {

    public Factor(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        Result x = null;
        final Token next = tokenizer.next();
        if (next.isOperator() && next.getToken().equals("(")) {
            x = new Expression(code, tokenizer, symbolTable).parse();
            handleCloseBracket(); //$TODO$ check bracketed additions and multiplications
        } else if (next.isConstant()) {
            Constant nextConstant = (Constant) next;
            x = new Result(Kind.CONSTANT, null, nextConstant.value(), null, null, null);
        } else if (next.isDesignator()) {
            //$TODO$ implement lookup and set the address instead of 0
            Symbol recent = getSymbolTable().getRecentOccurence(next.getToken());
            if(recent.getType().isArray()) {
                x = new Result(Kind.ARRAY);
                x.setArrayIdentifiers(accumulateArrayIdentifiers(recent));
            } else if(recent.getType().isVariable()) {
                x = new Result(Kind.VAR);
            } else if(recent.getType().isProcedure()) {
                x = new Result(Kind.PROCEDURE);
            }
            x.setVariableName(next.getToken());
        } else if (next.isKeyword() && ((Keyword) next).isCall()) {
            x = new FunctionCall(code, tokenizer, symbolTable).parse();
        }
        return x;
    }

    private void handleCloseBracket() {
        final Token closeBoxBracket = tokenizer.next();
        if (!closeBoxBracket.getToken().equals(")")) {
            tokenizer.previous();
        } else {
            int x = 2;
        }
    }
}
