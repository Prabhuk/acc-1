package com.acc.structure;

import java.util.*;

/**
 * Created by prabhuk on 2/9/2015.
 */
public class SymbolTable {

    private final List<Symbol> symbols;
    private final Map<String, Symbol> symbolsByName;
    private SymbolTable globalSymbolTable;

    public SymbolTable() {
        symbols = new ArrayList<Symbol>();
        symbolsByName = new HashMap<String, Symbol>();
    }

    public int getFramePointer() {
        return this.symbols.size();
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

    public void removeSymbol(String symbolName, int suffix) {
        final Iterator<Symbol> iterator = symbols.iterator();
        while (iterator.hasNext()) {
            final Symbol next = iterator.next();
            if(next.getName().equals(symbolName) && next.getSuffix() == suffix) {
                iterator.remove();
            }
        }
    }

    public Symbol getRecentOccurence(String symbolName) {
        Symbol s = null;
        for (Symbol symbol : symbols) {
            if(symbol.getName().equals(symbolName)) {
                s = symbol;
            }
        }
        if(s == null && globalSymbolTable != null) {
            s = globalSymbolTable.getRecentOccurence(symbolName);
        }
        return s;
    }

    public Symbol getDeclaration(String symbolName) {
        Symbol s = null;
        for (Symbol symbol : symbols) {
            if(symbol.getName().equals(symbolName) && symbol.getSuffix() == -1) {
                return symbol;
            }
        }
        if(!globalSymbolTable.equals(this) && globalSymbolTable != null) {
            s = globalSymbolTable.getDeclaration(symbolName);
        }
        return s;
    }

    private String getSymbolKey(Symbol s) {
        return s.getName() + s.getSuffix();
    }

    public int getOffset(Symbol s) {
        return symbols.indexOf(s);
    }

    public SymbolTable getGlobalSymbolTable() {
        return globalSymbolTable;
    }

    public void setGlobalSymbolTable(SymbolTable globalSymbolTable) {
        this.globalSymbolTable = globalSymbolTable;
    }

    public Symbol getTargetSymbol(Symbol symbol) {
        Symbol targetSymbol = null;
        for (Symbol symbol1 : symbols) {
            if (symbol1.getName().equals(symbol.getName())) {
                targetSymbol = symbol1;
            }
        }
        if(targetSymbol == null && globalSymbolTable != null) {
            targetSymbol = globalSymbolTable.getTargetSymbol(symbol);
        }

        return targetSymbol;
    }
}
