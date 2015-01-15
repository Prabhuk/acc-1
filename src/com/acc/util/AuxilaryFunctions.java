package com.acc.util;

/**
 * Created by prabhuk on 1/14/2015.
 */
public class AuxilaryFunctions {

    public static void putF1(Code code, Opcodes op, String a, String b, String c) {
        StringBuilder codeBuilder = new StringBuilder();;
        codeBuilder.append(op.name())
                .append(" ")
                .append(a)
                .append(", ")
                .append(b)
                .append(", ")
                .append(c);
        code.addCode(codeBuilder.toString());
    }

    public static void main(String[] args) {
        final Code code = new Code();
        AuxilaryFunctions.putF1(code, Opcodes.ADD, "R1", "R2", "#5");
        System.out.println(code.toString());
    }
}
