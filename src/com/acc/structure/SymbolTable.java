package com.acc.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 2/9/2015.
 */
public class SymbolTable {

    private final List<Symbol> symbols;
    private final Map<String, Symbol> symbolsByName;

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

    public Symbol getDeclaration(String symbolName) {
        Symbol s = null;
        for (Symbol symbol : symbols) {
            if(symbol.getName().equals(symbolName) && symbol.getSuffix() == -1) {
                return symbol;
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
