package com.acc.structure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by prabhuk on 2/9/2015.
 */
public class SymbolTable {

    private final boolean isGlobalTable;
    private final String ownerProcedure; //Will be set for procedures which will have their own symbol table.
    private final Set<Symbol> symbols;
    private final Map<String, Symbol> symbolsByName = new HashMap<String, Symbol>();
    private static volatile SymbolTable globalTable;

    private SymbolTable() {
        this.isGlobalTable = true;
        this.ownerProcedure = null;
        symbols = new HashSet<Symbol>();
    }

    public static SymbolTable getGlobalSymbolTable() {
        synchronized (SymbolTable.class) {
            if (globalTable == null) {
                globalTable = new SymbolTable();
            }
        }
        return globalTable;
    }

    public SymbolTable(String ownerProcedure) {
        this.isGlobalTable = false;
        if (ownerProcedure == null) {
            throw new RuntimeException("You must associate the SymbolTable with a procedure.");
        }
        this.ownerProcedure = ownerProcedure;
        symbols = new HashSet<Symbol>();
    }

    private boolean isGlobalTable() {
        return isGlobalTable;
    }

    public String getOwnerProcedure() {
        return ownerProcedure;
    }

    public Set<Symbol> getSymbols() {
        return symbols;
    }


    /**
     * Adds the symbol to the symbol table if it does not exist already. If it does, then throws an error.
     * @param s
     */
    public void addSymbol(Symbol s) {
        if (s == null) {
            throw new RuntimeException("Symbol cannot be null");
        }

        final String symbolKey = getSymbolKey(s);
        final Symbol targetSymbol = symbolsByName.get(symbolKey);
        if (targetSymbol == null) {
            symbols.add(s);
            symbolsByName.put(symbolKey, s);
        } else {
            //targetSymbol.setValue(s.getValue());
            throw new RuntimeException("Duplicate Symbol addition for [" + symbolKey + "]");
        }
    }

    public void removeSymbol(Symbol s) {
        if (s == null) {
            return; //Fail quietly
        }
        final String symbolKey = getSymbolKey(s);
        final Symbol targetSymbol = symbolsByName.get(symbolKey);
        if (targetSymbol == null) {
            return; //Fail quietly
        } else {
            symbols.remove(s);
            symbolsByName.remove(symbolKey);
        }
    }
    private String getSymbolKey(Symbol s) {
        return s.getName() + s.getSuffix();
    }
}
