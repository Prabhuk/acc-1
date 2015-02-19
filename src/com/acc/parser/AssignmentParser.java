package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.structure.SymbolType;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
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
        final Token symbolName = tokenizer.next();

        if (!symbolName.isDesignator()) {
            throw new SyntaxErrorException("Designator expected. Found[" + symbolName.getToken() + "] instead");
        }
        final Symbol recent = getSymbolTable().getRecentOccurence(symbolName.getToken());
        List<Result> arrayIdentifiers = accumulateArrayIdentifiers(recent, code, tokenizer);
        final Token assignmentOp = tokenizer.next();
        if (!assignmentOp.isAssignmentOperator()) {
            throw new SyntaxErrorException("Assignment operator [<-] expected. Found[" + assignmentOp.getToken() + "] instead");
        }
        Result x = new Expression(code, tokenizer).parse();
        assignSymbol(code, symbolName.getToken(), recent, x, arrayIdentifiers, getSymbolTable());
        return x;
    }

    public static void assignToSymbol(Code code, Symbol rhs, Symbol lhs, SymbolTable symbolTable) {
        lhs.setValue(Symbol.cloneValue(rhs.getValue()));
        AuxiliaryFunctions.putMOV(code, rhs, lhs);
        symbolTable.addSymbol(lhs);
    }

    protected static void assignSymbol(Code code, String symbolName, Symbol recent, Result x,
                                       List<Result> arrayIdentifiers, SymbolTable symbolTable) {
        Symbol rhs;
        if (x.kind().isConstant()) {
            rhs = new Symbol();
            rhs.setValue(x.value());
            rhs.setPointerValue(false);
        } else {
            //$TODO$ what if not declared in the scope?
            rhs = new Symbol(symbolName, recent.getSuffix(), recent.getType(), true, null);
        }
        Symbol lhs = new Symbol(symbolName, code.getPc(), recent.getType(), !x.kind().isConstant(), null);
        if (recent.getType().isArray()) {
            lhs.setArrayDimension(recent.getArrayDimension());
            lhs.setArrayIdentifiers(arrayIdentifiers);
        }
        rhs.setResult(x);
        assignToSymbol(code, rhs, lhs, symbolTable);
    }

    protected static List<Result> accumulateArrayIdentifiers(Symbol recent, Code code, Tokenizer tokenizer) {
        List<Result> arrayIdentifiers = new ArrayList<Result>();
        if (recent.getType().isArray()) {
            for (int i = 0; i < recent.getArrayDimension(); i++) {
                handleOpenBoxBracket(tokenizer);
                arrayIdentifiers.add(new Expression(code, tokenizer).parse());
                handleCloseBoxBracket(tokenizer);
            }
        }
        return arrayIdentifiers;
    }

    private static void handleOpenBoxBracket(Tokenizer tokenizer) {
        final Token openBoxBracket = tokenizer.next();
        if (!openBoxBracket.getToken().equals("[")) {
            throw new SyntaxErrorException("Expected '['. Found[" + openBoxBracket.getToken() + "] instead");
        }
    }

    private static void handleCloseBoxBracket(Tokenizer tokenizer) {
        final Token closeBoxBracket = tokenizer.next();
        if (!closeBoxBracket.getToken().equals("]")) {
            throw new SyntaxErrorException("Expected ']'. Found[" + closeBoxBracket.getToken() + "] instead");
        }
    }
}
