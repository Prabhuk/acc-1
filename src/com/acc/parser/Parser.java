package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.SymbolTable;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 1/24/2015.
 */
public abstract class Parser {
    protected Code code;
    protected Tokenizer tokenizer;
    private SymbolTable symbolTable;
    private static final SymbolTable globalTable = SymbolTable.getGlobalSymbolTable();
    private final Map<String, List<String>> procedureArguments = new HashMap<String, List<String>>();

    public Parser(Code code, Tokenizer tokenizer) {
        this.code = code;
        this.tokenizer = tokenizer;
    }

    public static SymbolTable getGlobalTable() {
        return globalTable;
    }

    public Code getCode() {
        return code;
    }

    public SymbolTable getSymbolTable() {
        if (symbolTable != null) {
            return symbolTable;
        }
        return globalTable;
    }

    public void removeLocalSymbolTable() {
        symbolTable = null;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public Map<String, List<String>> getProcedureArguments() {
        return procedureArguments;
    }

    public List<String> getArgumentNamesForProcedure(String procedureName) {
        return procedureArguments.get(procedureName);
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
}
