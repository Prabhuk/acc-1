package com.acc.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 2/9/2015.
 */
public class SymbolTable {

    private final boolean isGlobalTable;
    private final String ownerProcedure; //Will be set for procedures which will have their own symbol table.
    private final List<Symbol> symbols;
    private final Map<String, Symbol> symbolsByName = new HashMap<String, Symbol>();
    private static volatile SymbolTable globalTable;

    private SymbolTable() {
        this.isGlobalTable = true;
        this.ownerProcedure = null;
        symbols = new ArrayList<Symbol>();
    }

    public int getFramePointer() {
        return this.symbols.size();
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
        symbols = new ArrayList<Symbol>();
    }

    public boolean isGlobalTable() {
        return isGlobalTable;
    }

    public String getOwnerProcedure() {
        return ownerProcedure;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }


    /**
     * Adds the symbol to the symbol table if it does not exist already. If it does, then throws an error.
     *
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

    public Symbol getRecentOccurence(String symbolName) {
        Symbol s = null;
        for (Symbol symbol : symbols) {
            if(symbol.getName().equals(symbolName)) {
                s = symbol;
            }
        }
        return s;
    }

    private String getSymbolKey(Symbol s) {
        return s.getName() + s.getSuffix();
    }

    public int getOffset(Symbol s) {
        return symbols.indexOf(s);
    }
}
