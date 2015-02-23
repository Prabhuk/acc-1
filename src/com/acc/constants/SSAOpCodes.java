package com.acc.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 2/20/2015.
 */
public class SSAOpCodes {
    public static final int ADD = 0;
    public static final int SUB = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;
    public static final int CMP = 5;

    public static final int LDW = 32;
    public static final int LDX = 33;
    public static final int STW = 36;
    public static final int STX = 37;

    public static final int MOV = 15; //This is not part of the opcodes supported by DLX architecture.
    public static final int PHI = 64; //And 64 is never converted to Integer instruction
    public static final int KILL = 65; //And 64 is never converted to Integer instruction

    public static final int BEQ = 40;
    public static final int BNE = 41;
    public static final int BLT = 42;
    public static final int BGE = 43;
    public static final int BLE = 44;
    public static final int BGT = 45;
        

    public static final int RDD = 50;
    public static final int WRD = 51;
    public static final int WRH = 52;
    public static final int WRL = 53;

    public static final Map<Integer, String> opcodeAndNames = new HashMap<Integer, String>();
    static {
        opcodeAndNames.put(0, "add");
        opcodeAndNames.put(1, "sub");
        opcodeAndNames.put(2, "mul");
        opcodeAndNames.put(3, "div");
        opcodeAndNames.put(5, "cmp");

        opcodeAndNames.put(32, "load");
        opcodeAndNames.put(33, "load");
        opcodeAndNames.put(36, "store");
        opcodeAndNames.put(37, "store");

        opcodeAndNames.put(15, "move");
        opcodeAndNames.put(64, "phi");
        opcodeAndNames.put(65, "kill");


        opcodeAndNames.put(40, "beq");
        opcodeAndNames.put(41, "bne");
        opcodeAndNames.put(42, "blt");
        opcodeAndNames.put(43, "bge");
        opcodeAndNames.put(44, "ble");
        opcodeAndNames.put(45, "bgt");

        opcodeAndNames.put(50, "read");
        opcodeAndNames.put(51, "write");
        opcodeAndNames.put(52, "write");
        opcodeAndNames.put(53, "writeNL");
    }

    public static List<Integer> excludeB = new ArrayList<Integer>();
    public static List<Integer> excludeA = new ArrayList<Integer>();

    static {
        excludeA.add(SSAOpCodes.RDD);
        excludeA.add(SSAOpCodes.WRL);
        excludeB.add(SSAOpCodes.BEQ);
        excludeB.add(SSAOpCodes.BNE);
        excludeB.add(SSAOpCodes.BLT);
        excludeB.add(SSAOpCodes.BGE);
        excludeB.add(SSAOpCodes.BLE);
        excludeB.add(SSAOpCodes.BGT);

        excludeB.add(SSAOpCodes.RDD);
        excludeB.add(SSAOpCodes.WRD);
        excludeB.add(SSAOpCodes.WRH);
        excludeB.add(SSAOpCodes.WRL);
    }
    
    public static class InstructionProperties {
        private String name;
        private int operandCount;

        public InstructionProperties(String name, int operandCount) {
            this.name = name;
            this.operandCount = operandCount;
        }

        public String getName() {
            return name;
        }

        public int getOperandCount() {
            return operandCount;
        }
    }

    public static String neg = "neg";
    public static String add = "add";
    public static String sub = "sub";
    public static String mul = "mul";
    public static String div = "div";
    public static String cmp = "cmp";
    public static String adda = "adda";
    public static String load = "load";
    public static String store = "store";
    public static String move = "move";
    public static String phi = "phi";
    public static String end = "end";
    public static String bra = "bra";
    public static String bne = "bne";
    public static String beq = "beq";
    public static String ble = "ble";
    public static String blt = "blt";
    public static String bge = "bge";
    public static String bgt = "bgt";
    public static String read = "read";
    public static String write = "write";
    public static String writenl = "writenl";
    public static String kill = "kill";

    public static final Map<String, Integer> intermediatesOperandCount = new HashMap<String, Integer>();
    
    static {
        
        intermediatesOperandCount.put(neg, 1);
        intermediatesOperandCount.put(add, 2);
        intermediatesOperandCount.put(sub, 2);
        intermediatesOperandCount.put(mul, 2);
        intermediatesOperandCount.put(div, 2);
        intermediatesOperandCount.put(cmp, 2);
        intermediatesOperandCount.put(adda, 2);
        intermediatesOperandCount.put(load, 1);
        intermediatesOperandCount.put(store, 1);
        intermediatesOperandCount.put(move, 2);
        intermediatesOperandCount.put(phi, -1);
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

    }
    
}
