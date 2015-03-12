package com.acc.codeGen;

import com.acc.constants.OperationCode;
import com.acc.data.Result;

/**
 * Created by Rumpy on 04-03-2015.
 */
public class MachineInstruction {
    protected Integer DLXinstruction;
    protected final Integer opcode;
    protected Integer location;
    protected int a;
    protected int b;
    protected int c;


    protected int machineCode;

    public MachineInstruction(int op, int a, int b, int c,int location, int machineCode) {
        this.opcode = op;
        this.a=a;
        this.b = b;
        this.c = c;
        this.machineCode = machineCode;
        this.location = location;
    }
    public Integer getLocation() {
        return location;
    }

    public void fixup(int address) {

            c=address;
            machineCode = (machineCode>>16) << 16 | address;


    }
    public int getMachineCode() {
        return machineCode;
    }




}
