package com.acc.vm;

import com.acc.constants.OperationCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prabhuk on 3/12/2015.
 */
public class OpcodeMapper {
    public static Map<Integer, Integer> noOperands = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> oneOperand = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> dlxOperandCounts = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> arithmetic = new HashMap<Integer, Integer>();
    public static Map<Integer, Integer> branch = new HashMap<Integer, Integer>();

    static {
        dlxOperandCounts.put(DLX.BSR, 1);
        dlxOperandCounts.put(DLX.RDI, 1);
        dlxOperandCounts.put(DLX.WRD, 1);
        dlxOperandCounts.put(DLX.WRH, 1);
        dlxOperandCounts.put(DLX.RET, 1);
        dlxOperandCounts.put(DLX.JSR, 1);


        dlxOperandCounts.put(DLX.CHKI, 2);
        dlxOperandCounts.put(DLX.BEQ, 2);
        dlxOperandCounts.put(DLX.BNE, 2);
        dlxOperandCounts.put(DLX.BLT, 2);
        dlxOperandCounts.put(DLX.BGE, 2);
        dlxOperandCounts.put(DLX.BLE, 2);
        dlxOperandCounts.put(DLX.BGT, 2);
        dlxOperandCounts.put(DLX.CHK, 2);

        dlxOperandCounts.put(DLX.WRL, 0);


        noOperands.put(OperationCode.read, DLX.RDI);
        noOperands.put(OperationCode.writenl, DLX.WRL);

        arithmetic.put(OperationCode.add, DLX.ADD);
        arithmetic.put(OperationCode.sub, DLX.SUB);
        arithmetic.put(OperationCode.mul, DLX.MUL);
        arithmetic.put(OperationCode.div, DLX.DIV);
        arithmetic.put(OperationCode.cmp, DLX.CMP);

        branch.put(OperationCode.bra, DLX.BSR);
        branch.put(OperationCode.beq, DLX.BEQ);
        branch.put(OperationCode.bne, DLX.BNE);
        branch.put(OperationCode.blt, DLX.BLT);
        branch.put(OperationCode.bge, DLX.BGE);
        branch.put(OperationCode.ble, DLX.BLE);
        branch.put(OperationCode.bgt, DLX.BGT);


    }


    public static Integer getDLXoperandCount(Integer opcode) {
        final Integer count = dlxOperandCounts.get(opcode);
        return count == null ? 3 : count;
    }


}
