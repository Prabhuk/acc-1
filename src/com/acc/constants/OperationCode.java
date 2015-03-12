package com.acc.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class OperationCode {

    public static int neg = 0;
    public static int add = 1;//
    public static int sub = 2;//
    public static int mul = 3;//
    public static int div = 4;//
    public static int cmp = 5;//
    public static int adda = 6;
    public static int load = 7;
    public static int store = 8;
    public static int move = 9;
    public static int phi = 10;
    public static int end = 11;//
    public static int bra = 12;//
    public static int bne = 13;//
    public static int beq = 14;//
    public static int ble = 15;//
    public static int blt = 16;//
    public static int bge = 17;//
    public static int bgt = 18;//
    public static int read = 19;//
    public static int write = 20;//
    public static int writenl = 21;//
    public static int kill = 22;
    public static int call = 23;//
    public static int ret = 24;//

    public static final Map<Integer, Integer> intermediatesOperandCount = new HashMap<Integer, Integer>();
    public static final Map<Integer, String> opcodeNames = new HashMap<Integer, String>();

    static {
        intermediatesOperandCount.put(neg, 1);
        intermediatesOperandCount.put(add, 2);
        intermediatesOperandCount.put(sub, 2);
        intermediatesOperandCount.put(mul, 2);
        intermediatesOperandCount.put(div, 2);
        intermediatesOperandCount.put(cmp, 2);
        intermediatesOperandCount.put(adda, 2);
        intermediatesOperandCount.put(load, 1);
        intermediatesOperandCount.put(store, 2);
        intermediatesOperandCount.put(move, 2);
        intermediatesOperandCount.put(phi, 2);
        intermediatesOperandCount.put(end, 0);
        intermediatesOperandCount.put(bra, 1);
        intermediatesOperandCount.put(bne, 2);
        intermediatesOperandCount.put(beq, 2);
        intermediatesOperandCount.put(ble, 2);
        intermediatesOperandCount.put(blt, 2);
        intermediatesOperandCount.put(bge, 2);
        intermediatesOperandCount.put(bgt, 2);
        intermediatesOperandCount.put(read, 0);
        intermediatesOperandCount.put(write, 1);
        intermediatesOperandCount.put(writenl, 0);
        intermediatesOperandCount.put(kill, 1);
        intermediatesOperandCount.put(call, 1);
        intermediatesOperandCount.put(ret, 1);

        opcodeNames.put(neg,"neg");
        opcodeNames.put(add,"add");
        opcodeNames.put(sub,"sub");
        opcodeNames.put(mul,"mul");
        opcodeNames.put(div, "div");
        opcodeNames.put(cmp, "cmp");
        opcodeNames.put(adda, "adda");
        opcodeNames.put(load, "load");
        opcodeNames.put(store, "store");
        opcodeNames.put(move,"move");
        opcodeNames.put(phi,"phi");
        opcodeNames.put(end,"end");
        opcodeNames.put(bra,"bra");
        opcodeNames.put(bne, "bne");
        opcodeNames.put(beq, "beq");
        opcodeNames.put(ble, "ble");
        opcodeNames.put(blt,"blt");
        opcodeNames.put(bge, "bge");
        opcodeNames.put(bgt, "bgt");
        opcodeNames.put(read, "read");
        opcodeNames.put(write, "write");
        opcodeNames.put(writenl, "writenl");
        opcodeNames.put(kill, "kill");
        opcodeNames.put(call, "call");
        opcodeNames.put(ret, "return");

        
    }

    public static boolean isConditionSatisfied(int branchCode, Integer cmpResult) {

     switch (branchCode) {
         case 13:
             return cmpResult != 0;
         case 14:
             return cmpResult == 0;
         case 15:
             return cmpResult <= 0;
         case 16:
             return cmpResult <= 0;
         case 17:
             return cmpResult >= 0;
         case 18:
             return cmpResult > 0;
     }
        return false;
    }
    
    

    public static Integer getOperandCount(Integer op) {
        return intermediatesOperandCount.get(op);
    }

    public static String getOperationName(Integer op) {
        return opcodeNames.get(op);
    }
    
}
