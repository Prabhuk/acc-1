package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.SymbolType;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class VariableDeclaration extends Parser {

    /**
     * Handles one or more variable declarations { varDecl } where varDecl = typeDecl indent { “,” ident } “;”
     * @param code
     * @param tokenizer
     */
    public VariableDeclaration(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Token type = tokenizer.next();
        //$TODO$ extract TypeDeclaration. Handle multidimensional arrays
        if(!isVarOrArrayKeyword(type)) {
            throw new SyntaxErrorException("Unexpected token ["+type.getToken()+"]. Exptected keyword [var] or [array]");
        }
        Keyword _type = (Keyword) type;
        final SymbolType symbolType = _type.isArray() ? SymbolType.ARRAY : SymbolType.VARIABLE;
        declareSymbol(symbolType);
        Token next = tokenizer.next();
        while (next.getToken().equals(",")) {
            declareSymbol(symbolType);
        }

        if(!next.getToken().equals(";")) {
            throw new SyntaxErrorException("Expected \";\" Found [" + next.getToken() + "] instead");
        }

        type = tokenizer.next();
        while(isVarOrArrayKeyword(type)) {
            tokenizer.previous();
            parse();
        }
        tokenizer.previous();
        return null;
    }

    private boolean isVarOrArrayKeyword(Token type) {
        return type.isKeyword() && (!((Keyword)type).isVar() || !((Keyword)type).isArray());
//        return !type.isKeyword() || (!((Keyword)type).isVar() && !((Keyword)type).isArray());
    }

    private void declareSymbol(SymbolType symbolType) {
        Token symbolName = tokenizer.next();
        if(!symbolName.isDesignator()) {
            throw new SyntaxErrorException("Identifier expected. Found ["+symbolName.getToken()+"] instead");
        }
        //$TODO$ handle multidimensional array.
        AuxiliaryFunctions.declareSymbol(code, symbolName.getToken(), getSymbolTable(), symbolType);
    }
}
