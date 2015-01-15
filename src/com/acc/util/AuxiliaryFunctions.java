package com.acc.util;

import com.acc.data.Code;
import com.acc.data.Opcodes;
import com.acc.data.Result;

/**
 * Created by prabhuk on 1/14/2015.
 * The auxilary methods to cre
 */
public class AuxiliaryFunctions {

    private Code code;

    public AuxiliaryFunctions(Code code) {
        this.code = code;
    }

    public void putF1(Opcodes op, String a, String b, String c) {
        StringBuilder codeBuilder = new StringBuilder();
        codeBuilder.append(op.name());
        boolean addComma = false;
        if (a != null) {
            codeBuilder.append(" ")
                    .append(a);
            addComma = true;
        }
        if (b != null) {
            if (addComma) {
                codeBuilder.append(", ");
            }
            codeBuilder.append(" ")
                    .append(b);
            addComma = true;
        }
        if (c != null) {
            if (addComma) {
                codeBuilder.append(", ");
            }
            codeBuilder.append(" ")
                    .append(c);

        }
        code.addCode(codeBuilder.toString());
    }

    public void BJ(int loc) {
        putF1(Opcodes.BEQ, "0", null, String.valueOf(loc - code.getPc()));
    }

    public void FJLink(Result x) {
        putF1(Opcodes.BEQ, "0", null, String.valueOf(x.getFixuploc()));
        x.setFixuploc(code.getPc() - 1);
    }

//    public void CJF(Result x) {
//        putF1(Opcodes.BEQ + negated[x.getCond()], x.getRegno(), 0, offset);
//        x.setFixuploc(pc - 1);
//    }

    public static void main(String[] args) {
        final Code code = new Code();
        AuxiliaryFunctions auxf = new AuxiliaryFunctions(code);
        auxf.putF1(Opcodes.ADD, "R1", "R2", "#5");
        System.out.println(code.toString());
    }
}
