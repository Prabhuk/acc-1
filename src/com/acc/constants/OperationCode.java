package com.acc.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class OperationCode {

    public static final int ADD = 0;
    public static final int SUB = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;
    public static final int MOD = 4;
    public static final int CMP = 5;
    public static final int OR = 8;
    public static final int AND = 9;
    public static final int BIC = 10;
    public static final int XOR = 11;
    public static final int LSH = 12;
    public static final int ASH = 13;
    public static final int CHK = 14;

    public static final int ADDI = 16;
    public static final int SUBI = 17;
    public static final int MULI = 18;
    public static final int DIVI = 19;
    public static final int MODI = 20;
    public static final int CMPI = 21;
    public static final int ORI = 24;
    public static final int ANDI = 25;
    public static final int BICI = 26;
    public static final int XORI = 27;
    public static final int LSHI = 28;
    public static final int ASHI = 29;
    public static final int CHKI = 30;

    public static final int LDW = 32;
    public static final int LDX = 33;
    public static final int POP = 34;
    public static final int STW = 36;
    public static final int STX = 37;
    public static final int PSH = 38;

    public static final int BEQ = 40;
    public static final int BNE = 41;
    public static final int BLT = 42;
    public static final int BGE = 43;
    public static final int BLE = 44;
    public static final int BGT = 45;
    public static final int BSR = 46;
    public static final int JSR = 48;
    public static final int RET = 49;

    public static final int RDI = 50;
    public static final int WRD = 51;
    public static final int WRH = 52;
    public static final int WRL = 53;

    public static final int ERR = 63;

    /**
     * Dummy instruction code to represent mov
     */
    public static final int MOV = 15; //This is not part of the opcodes supported by DLX architecture.
    public static final int PHI = 64; //And 64 is never converted to Integer instruction
    public static final int KILL = 65; //And 64 is never converted to Integer instruction

    public static final int MOV_CONSTANT = 0;
    public static final int MOV_VARIABLE = 1;

    public static final Map<Integer, String> opcodeAndNames = new HashMap<Integer, String>();

    static {
        opcodeAndNames.put(0, "ADD");
        opcodeAndNames.put(1, "SUB");
        opcodeAndNames.put(2, "MUL");
        opcodeAndNames.put(3, "DIV");
        opcodeAndNames.put(4, "MOD");
        opcodeAndNames.put(5, "CMP");
        opcodeAndNames.put(8, "OR");
        opcodeAndNames.put(9, "AND");
        opcodeAndNames.put(10, "BIC");
        opcodeAndNames.put(11, "XOR");
        opcodeAndNames.put(12, "LSH");
        opcodeAndNames.put(13, "ASH");
        opcodeAndNames.put(14, "CHK");

        opcodeAndNames.put(16, "ADDI");
        opcodeAndNames.put(17, "SUBI");
        opcodeAndNames.put(18, "MULI");
        opcodeAndNames.put(19, "DIVI");
        opcodeAndNames.put(20, "MODI");
        opcodeAndNames.put(21, "CMPI");
        opcodeAndNames.put(24, "ORI");
        opcodeAndNames.put(25, "ANDI");
        opcodeAndNames.put(26, "BICI");
        opcodeAndNames.put(27, "XORI");
        opcodeAndNames.put(28, "LSHI");
        opcodeAndNames.put(29, "ASHI");
        opcodeAndNames.put(30, "CHKI");

        opcodeAndNames.put(32, "LDW");
        opcodeAndNames.put(33, "LDX");
        opcodeAndNames.put(34, "POP");
        opcodeAndNames.put(36, "STW");
        opcodeAndNames.put(37, "STX");
        opcodeAndNames.put(38, "PSH");
        opcodeAndNames.put(40, "BEQ");
        opcodeAndNames.put(41, "BNE");
        opcodeAndNames.put(42, "BLT");
        opcodeAndNames.put(43, "BGE");
        opcodeAndNames.put(44, "BLE");
        opcodeAndNames.put(45, "BGT");
        opcodeAndNames.put(46, "BSR");
        opcodeAndNames.put(48, "JSR");
        opcodeAndNames.put(49, "RET");

        opcodeAndNames.put(50, "RDD");
        opcodeAndNames.put(51, "WRD");
        opcodeAndNames.put(52, "WRH");
        opcodeAndNames.put(53, "WRL");

        opcodeAndNames.put(63, "ERR");


        opcodeAndNames.put(15, "MOV");
        opcodeAndNames.put(64, "PHI");
        opcodeAndNames.put(65, "KILL");

    }

    public static List<Integer> excludeB = new ArrayList<Integer>();
    public static List<Integer> excludeA = new ArrayList<Integer>();

    static {
        excludeA.add(OperationCode.BSR);
        excludeA.add(OperationCode.JSR);
        excludeA.add(OperationCode.RET);

        excludeB.add(OperationCode.CHK);
        excludeB.add(OperationCode.CHKI);
        excludeB.add(OperationCode.BEQ);
        excludeB.add(OperationCode.BNE);
        excludeB.add(OperationCode.BLT);
        excludeB.add(OperationCode.BGE);
        excludeB.add(OperationCode.BLE);
        excludeB.add(OperationCode.BGT);
        excludeB.add(OperationCode.BSR);
        excludeB.add(OperationCode.JSR);
        excludeB.add(OperationCode.RET);



//    OperationCode.RDD
//    OperationCode.WRD
//    OperationCode.WRH
//    OperationCode.WRL
    }

}
