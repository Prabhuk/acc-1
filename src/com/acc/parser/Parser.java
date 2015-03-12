package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.ui.OutputContents;
import com.acc.util.Tokenizer;

import java.util.*;

/**
 * Created by prabhuk on 1/24/2015.
 */
public abstract class Parser {
    protected Code code;
    protected Tokenizer tokenizer;
    protected final SymbolTable symbolTable;
    protected static final Map<String, Integer> predefinedProcedureArguments = new HashMap<String, Integer>();
    protected static final Result FP = new Result(Kind.FRAME_POINTER);
    protected static OutputContents outputContents;
    protected Set<String> globalVariablesUsed = new HashSet<String>();
    protected Set<String> functionsUsed = new HashSet<String>();


    static {
        FP.value(100); //FRAME POINTER ADDRESS
        //$TODO$ Cannot hardcode to 100. Needs fixing $Fixme$
        predefinedProcedureArguments.put("InputNum", 0);
        predefinedProcedureArguments.put("OutputNum", 1);
        predefinedProcedureArguments.put("OutputNewLine", 0);
    }

    public Parser(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        this.code = code;
        this.tokenizer = tokenizer;
        this.symbolTable = symbolTable;
    }

    public Code getCode() {
        return code;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }


    public List<String> getArgumentNamesForProcedure(String procedureName) {
        final Computation program = outputContents.getProgram(procedureName);
        return program.getFormalParams();
    }

    public abstract Result parse();

    protected List<Result> accumulateArrayIdentifiers(Symbol recent) {
        List<Result> arrayIdentifiers = new ArrayList<Result>();
        if (recent.getType().isArray()) {
            for (int i = 0; i < recent.getArrayDimension(); i++) {
                //$TODO$ Not necessary but can handle complete assignments of arrays here
                handleOpenBoxBracket();
                arrayIdentifiers.add(new Expression(code, tokenizer, symbolTable).parse());
                handleCloseBoxBracket();
            }
        }
        return arrayIdentifiers;
    }



    protected void handleOpenBoxBracket() {
        final Token openBoxBracket = tokenizer.next();
        if (!openBoxBracket.getToken().equals("[")) {
            throw new SyntaxErrorException("Expected '['. Found[" + openBoxBracket.getToken() + "] instead");
        }
    }

    protected void handleCloseBoxBracket() {
        final Token closeBoxBracket = tokenizer.next();
        if (!closeBoxBracket.getToken().equals("]")) {
            throw new SyntaxErrorException("Expected ']'. Found[" + closeBoxBracket.getToken() + "] instead");
        }
    }

    public OutputContents getOutputContents() {
        return outputContents;
    }

    public void setOutputContents(OutputContents outputContents) {
        this.outputContents = outputContents;
    }

    public Set<String> getGlobalVariablesUsed() {
        return globalVariablesUsed;
    }

    public void setGlobalVariablesUsed(Set<String> globalVariablesUsed) {
        this.globalVariablesUsed = globalVariablesUsed;
    }

    public Set<String> getFunctionsUsed() {
        return functionsUsed;
    }

    public void setFunctionsUsed(Set<String> functionsUsed) {
        this.functionsUsed = functionsUsed;
    }
}
