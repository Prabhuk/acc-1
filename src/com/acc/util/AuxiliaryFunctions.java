package com.acc.util;

import com.acc.data.Code;
import com.acc.data.Kind;
import com.acc.data.OperationCode;
import com.acc.data.Result;

/**
 * Created by prabhuk on 1/14/2015.
 * The auxilary methods to cre
 */
public class AuxiliaryFunctions {

    public static void putF1(Code code, int instructionCode, int a, int b, int c) {
        if (c < 0) c ^= 0xFFFF0000;
        code.addCode(instructionCode << 26 | a << 21 | b << 16 | c);
    }

    public static void putF2(Code code, int instructionCode, int a, int b, int c) {

        code.addCode(instructionCode << 26 | a << 21 | b << 16 | c);

    }

    public static void putF3(Code code, int instructionCode, int c) {
        code.addCode(instructionCode << 26 | c);
    }

    public static void BJ(Code code, int loc) {
        putF1(code, OperationCode.BEQ, 0, 0, loc - code.getPc());
    }

    public static void FJLink(Code code, Result x) {
        putF1(code, OperationCode.BEQ, 0, 0, x.fixupLoc());
        x.fixupLoc(code.getPc() - 1);
    }

//    public void CJF(Result x) {
//        putF1(Opcodes.BEQ + negated[x.condition()], x.regNo(), 0, offset);
//        x.fixupLoc(pc - 1);
//    }

    /*
     * Combines x & y and resultant result obj is maintained in x.
     */
    public static void combine(Code code, int op, Result x, Result y) {
        if(x.kind().isConst() && y.kind().isConst()) {
            if(op == OperationCode.ADD) {
                x.value(x.value() + y.value());
            } else if(op == OperationCode.SUB) {
                x.value(x.value() - y.value());
            } else if(op == OperationCode.MUL) {
                x.value(x.value() * y.value());
            } else if(op == OperationCode.DIV) {
                x.value(x.value() / y.value());
            } else {
                throw new UnsupportedOperationException("Combine cannot process Operation code ["+op+"]");
            }
            x.kind(Kind.CONST);
        } else {
            load(x);
            if(y.kind().isConst()) {
                putF1(code, op + 16, x.regNo(), x.regNo(), y.value());
            } else {
                load(y);
                putF1(code, op, x.regNo(), x.regNo(), y.regNo());
                deallocate(y.regNo());
            }
        }
    }

    private static void deallocate(int regno) {
        //$TODO$ needs implementation
    }

    private static void load(Result x) {
        //$TODO$ needs implementation
    }

    public static void main(String[] args) {
        final Code code = new Code();
        AuxiliaryFunctions.putF1(code, OperationCode.ADD, 1, 2, 5);
//        Printer.print(code.toString());
    }
}
