package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.structure.SymbolType;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class FunctionDeclaration extends Parser {

    public FunctionDeclaration(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        Token procedureName = tokenizer.next();
        if (!procedureName.isIdentifier()) {
            throw new SyntaxErrorException(procedureName.tokenType(), TokenType.IDENTIFIER);
        }
        //$TODO$ DEAL WITH ident
        final SymbolTable procedureSymbolTable = new SymbolTable();
        procedureSymbolTable.setGlobalSymbolTable(getSymbolTable());
        Token next = tokenizer.next();
        if (!isSemiColonToken(next)) {
            tokenizer.previous();
            if (!isOpenParanthesis(tokenizer.next())) {
                throw new SyntaxErrorException("Expected \"(\". Found [" + next.getToken() + "] instead");
            }
            next = tokenizer.next();
            while(!isClosedParanthesis(next)) {
                tokenizer.previous();
                final Result y = new FormalParam(code, tokenizer, symbolTable).parse();

                if(y != null) {
                    procedureSymbolTable.addSymbol(new Symbol(y.getVariableName(), -1, null, SymbolType.VARIABLE));
                }
                next = tokenizer.next();
            }
            next = tokenizer.next();
            if (!isSemiColonToken(next)) {
                throw new SyntaxErrorException("Expected \";\" keyword. Found [" + next.getToken() + "] instead");
            }
        }
        final Code procedureCode = new Code();
        procedureCode.setProgramName(procedureName.getToken());
        final Computation computation = new Computation(procedureCode, tokenizer, procedureSymbolTable, procedureName.getToken(), outputContents);
        computation.programBody();
        AuxiliaryFunctions.addInstruction(OperationCode.end, procedureCode, null, null, procedureSymbolTable);

        next = tokenizer.next();
        if (!isSemiColonToken(next)) {
            throw new SyntaxErrorException("Expected \";\" keyword. Found [" + next.getToken() + "] instead");
        }

        return null;
    }

    private boolean isSemiColonToken(Token next) {
        return next.getToken().equals(";");
    }

    private boolean isFunctionOrProcedureKeyword(Token next) {
        return next.isKeyword() && (((Keyword) next).isFunction() || ((Keyword) next).isProcedure());
    }

    private boolean isClosedParanthesis(Token next) {
        return next.getToken().equals(")");
    }

    private boolean isOpenParanthesis(Token next) {
        return next.getToken().equals("(");
    }
}
