package com.acc.vm;

import com.acc.constants.OperationCode;
import com.acc.data.Result;

/**
 * Created by prabhuk on 3/10/2015.
 */
public class DLXInstruction {
    int opcode;
    int a;
    int b;
    int c;

    public DLXInstruction(int opcode, int a, int b, int c) {
        this.opcode = opcode;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    private String getOperand(Result x) {

        return "";
    }
}
