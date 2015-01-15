package com.acc.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prabhuk on 1/14/2015.
 * This holds the generated code
 */
public class Code {

    private final List<String> code = new LinkedList<String>();

    /**
     * @return Returns the current program counter value
     */
    public int getPc() {
        return code.size();
    }

    /**
     *
     * @param code - Takes a line of code and appends to the output assembly/intermediate code
     * @return Returns the current program counter value
     */
    public int addCode(String code) {
        this.code.add(code);
        return this.code.size();
    }

    @Override
    public String toString() {
        final Iterator<String> iterator = code.iterator();
        StringBuilder finalStringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            finalStringBuilder.append(iterator.next()).append("\n");
        }
        return finalStringBuilder.toString();
    }

}
