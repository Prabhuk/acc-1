package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.structure.SymbolType;
import com.acc.util.Tokenizer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class VariableDeclaration extends Parser {

    public VariableDeclaration(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }



    @Override
    public Result parse() {
        Result x = new TypeDeclaration(code, tokenizer, symbolTable).parse();
        Kind _type = x.kind();
        if(_type == null) {
            tokenizer.previous();
            return x;
        }
        if(_type.isProcedure()) {
            x = new FunctionDeclaration(code, tokenizer, symbolTable).parse();
        } else {
            final SymbolType symbolType = _type.isArray() ? SymbolType.ARRAY : SymbolType.VARIABLE;
            declareSymbol(symbolType, x.dimensions());
            Token next = tokenizer.next();
            while (next.getToken().equals(",")) {
                declareSymbol(symbolType, x.dimensions());
                next = tokenizer.next();
            }

            if (!next.getToken().equals(";")) {
                throw new SyntaxErrorException("Expected \";\" Found [" + next.getToken() + "] instead");
            }
        }
        Token type = tokenizer.next();
        while (isVarOrArrayKeyword(type)) {
            tokenizer.previous();
            parse();
            type = tokenizer.next();
        }
        tokenizer.previous();
        return x;
    }

    private boolean isVarOrArrayKeyword(Token type) {
        return type.isKeyword() && (!((Keyword) type).isVar() || !((Keyword) type).isArray());
    }

    private void declareSymbol(SymbolType symbolType, List<Integer> arrayDimensions) {
        Token symbolName = tokenizer.next();
        if (!symbolName.isDesignator()) {
            throw new SyntaxErrorException("Identifier expected. Found [" + symbolName.getToken() + "] instead");
        }
        //$TODO$ handle multidimensional array. => In progress
        declareSymbol(symbolName.getToken(), symbolType, arrayDimensions);
    }

    public void declareSymbol(String symbolName, SymbolType type, List<Integer> arrayDimensions) {
        final Symbol s;
        if (type == SymbolType.ARRAY) {
            List<Result> originalArrayIdentifiers = new ArrayList<Result>();
            s = new Symbol(symbolName, -1, arrayDimensions.size(), null);
            final int dimensionCount = arrayDimensions.size();
            int[] dimensionsArray = new int[dimensionCount];
            for (int i = 0; i < arrayDimensions.size(); i++) {
                dimensionsArray[i] = arrayDimensions.get(i);
                final Result identifier = new Result(Kind.CONSTANT);
                identifier.value(arrayDimensions.get(i));
                originalArrayIdentifiers.add(identifier);
            }
            s.setArrayDimension(dimensionCount);
            s.setArrayIdentifiers(originalArrayIdentifiers);
            s.setArrayValue(Array.newInstance(Integer.class, dimensionsArray));
        } else {
            s = new Symbol(symbolName, -1,null, type);
        }
        symbolTable.addSymbol(s);
    }
}
