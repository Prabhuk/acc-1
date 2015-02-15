package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.SymbolType;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.List;

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
        //$TODO$ extract TypeDeclaration. Handle multidimensional arrays => In progress
        Result x = new TypeDeclaration(code,tokenizer).parse();
        Kind _type = x.kind();
        final SymbolType symbolType = _type.isArray() ? SymbolType.ARRAY : SymbolType.VARIABLE;
        declareSymbol(symbolType, x.dimensions());
        Token next = tokenizer.next();
        while (next.getToken().equals(",")) {
            declareSymbol(symbolType, x.dimensions());
            next = tokenizer.next();
        }

        if(!next.getToken().equals(";")) {
            throw new SyntaxErrorException("Expected \";\" Found [" + next.getToken() + "] instead");
        }

        Token type = tokenizer.next();
        while(isVarOrArrayKeyword(type)) {
            tokenizer.previous();
            parse();
            type = tokenizer.next();
        }
        tokenizer.previous();
        return null;
    }

    private boolean isVarOrArrayKeyword(Token type) {
        return type.isKeyword() && (!((Keyword)type).isVar() || !((Keyword)type).isArray());
    }

    private void declareSymbol(SymbolType symbolType, List<Integer> arrayDimensions) {
        Token symbolName = tokenizer.next();
        if(!symbolName.isDesignator()) {
            throw new SyntaxErrorException("Identifier expected. Found ["+symbolName.getToken()+"] instead");
        }
        //$TODO$ handle multidimensional array. => In progress
        AuxiliaryFunctions.declareSymbol(code, symbolName.getToken(), getSymbolTable(), symbolType, arrayDimensions);
    }
}
