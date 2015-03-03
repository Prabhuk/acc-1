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
    private static final Map<String, List<String>> procedureArguments = new HashMap<String, List<String>>();
    protected static final Result FP = new Result(Kind.FRAME_POINTER);
    protected static OutputContents outputContents;


    static {
        FP.value(100); //FRAME POINTER ADDRESS
        //$TODO$ Cannot hardcode to 100. Needs fixing $Fixme$
        procedureArguments.put("InputNum", Collections.EMPTY_LIST);
        final ArrayList<String> strings = new ArrayList<String>();
        strings.add("out");
        procedureArguments.put("OutputNum", strings);
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


    public Map<String, List<String>> getProcedureArguments() {
        return procedureArguments;
    }

    public List<String> getArgumentNamesForProcedure(String procedureName) {
        final List<String> strings = procedureArguments.get(procedureName);
        return strings == null ? Collections.EMPTY_LIST : strings;
    }

    /**
     * For the given procedureName, stores the list of argument names in order.
     * throws SyntaxErrorException when duplicate argument name is passed
     *
     * @param procedureName
     * @param argumentName
     * @return true on successful addition of argument name, false otherwise
     */
    public boolean addArgumentNameForProcedure(String procedureName, String argumentName) {
        if (procedureArguments.get(procedureName) == null) {
            procedureArguments.put(procedureName, new ArrayList<String>());
        }

        List<String> args = procedureArguments.get(procedureName);
        if (args.contains(argumentName)) {
            throw new SyntaxErrorException("Duplicate argument name: [" + argumentName + "] in the procedure [" + procedureName + "]");
        }

        return args.add(argumentName);
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
}
