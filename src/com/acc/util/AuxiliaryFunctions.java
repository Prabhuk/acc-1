package com.acc.util;

import com.acc.data.Code;
import com.acc.data.OperationCode;
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

    public void putF1(int instructionCode, int a, int b, int c) {
        if (c < 0) c ^= 0xFFFF0000;
        code.addCode(instructionCode << 26 | a << 21 | b << 16 | c);
    }

    public void putF2(int instructionCode, int a, int b, int c) {

        code.addCode(instructionCode << 26 | a << 21 | b << 16 | c);

    }

    public void putF3(int instructionCode, int c) {
        code.addCode(instructionCode << 26 | c);
    }

    public void BJ(int loc) {
        putF1(OperationCode.BEQ, 0, 0, loc - code.getPc());
    }

    public void FJLink(Result x) {
        putF1(OperationCode.BEQ, 0, 0, x.getFixuploc());
        x.setFixuploc(code.getPc() - 1);
    }

//    public void CJF(Result x) {
//        putF1(Opcodes.BEQ + negated[x.getCond()], x.getRegno(), 0, offset);
//        x.setFixuploc(pc - 1);
//    }

    public static void main(String[] args) {
        final Code code = new Code();
        AuxiliaryFunctions auxf = new AuxiliaryFunctions(code);
        auxf.putF1(OperationCode.ADD, 1, 2, 5);
//        Printer.print(code.toString());
    }
}
