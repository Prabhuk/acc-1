package com.acc.codeGen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rumpy on 03-03-2015.
 */
public class MachineOperationCode {

        public static final int ADD = 0;//
        public static final int SUB = 1;//
        public static final int MUL = 2;//
        public static final int DIV = 3;//
        public static final int MOD = 4;//
        public static final int CMP = 5;//
        public static final int OR = 8;
        public static final int AND = 9;
        public static final int BIC = 10;
        public static final int XOR = 11;
        public static final int LSH = 12;
        public static final int ASH = 13;
        public static final int CHK = 14;

        public static final int ADDI = 16;//
        public static final int SUBI = 17;//
        public static final int MULI = 18;//
        public static final int DIVI = 19;//
        public static final int MODI = 20;//
        public static final int CMPI = 21;//
        public static final int ORI = 24;
        public static final int ANDI = 25;
        public static final int BICI = 26;
        public static final int XORI = 27;
        public static final int LSHI = 28;
        public static final int ASHI = 29;
        public static final int CHKI = 30;

        public static final int LDW = 32;
        public static final int LDX = 33;
        public static final int POP = 34;//
        public static final int STW = 36;
        public static final int STX = 37;
        public static final int PSH = 38;//

        public static final int BEQ = 40;
        public static final int BNE = 41;
        public static final int BLT = 42;
        public static final int BGE = 43;
        public static final int BLE = 44;
        public static final int BGT = 45;
        public static final int BSR = 46;//
        public static final int JSR = 48;
        public static final int RET = 49;//

        public static final int RDD = 50; //
        public static final int WRD = 51; //
        public static final int WRH = 52; //
        public static final int WRL = 53; //

        public static final int ERR = 63;

        public static final Map<Integer, String> opcodeNames = new HashMap<Integer, String>();
        static
        {
                opcodeNames.put(	ADD 	,	"	ADD 	");
                opcodeNames.put(	SUB 	,	"	SUB 	");
                opcodeNames.put(	MUL 	,	"	MUL 	");
                opcodeNames.put(	DIV 	,	"	DIV 	");
                opcodeNames.put(	MOD 	,	"	MOD 	");
                opcodeNames.put(	CMP 	,	"	CMP 	");
                opcodeNames.put(	OR 	,	"	OR 	");
                opcodeNames.put(	AND 	,	"	AND 	");
                opcodeNames.put(	BIC 	,	"	BIC 	");
                opcodeNames.put(	XOR 	,	"	XOR 	");
                opcodeNames.put(	LSH 	,	"	LSH 	");
                opcodeNames.put(	ASH 	,	"	ASH 	");
                opcodeNames.put(	CHK 	,	"	CHK 	");
                opcodeNames.put(	ADDI 	,	"	ADDI 	");
                opcodeNames.put(	SUBI 	,	"	SUBI 	");
                opcodeNames.put(	MULI 	,	"	MULI 	");
                opcodeNames.put(	DIVI 	,	"	DIVI 	");
                opcodeNames.put(	MODI 	,	"	MODI 	");
                opcodeNames.put(	CMPI 	,	"	CMPI 	");
                opcodeNames.put(	ORI 	,	"	ORI 	");
                opcodeNames.put(	ANDI 	,	"	ANDI 	");
                opcodeNames.put(	BICI 	,	"	BICI 	");
                opcodeNames.put(	XORI 	,	"	XORI 	");
                opcodeNames.put(	LSHI 	,	"	LSHI 	");
                opcodeNames.put(	ASHI 	,	"	ASHI 	");
                opcodeNames.put(	CHKI 	,	"	CHKI 	");
                opcodeNames.put(	LDW 	,	"	LDW 	");
                opcodeNames.put(	LDX 	,	"	LDX 	");
                opcodeNames.put(	POP 	,	"	POP 	");
                opcodeNames.put(	STW 	,	"	STW 	");
                opcodeNames.put(	STX 	,	"	STX 	");
                opcodeNames.put(	PSH 	,	"	PSH 	");
                opcodeNames.put(	BEQ 	,	"	BEQ 	");
                opcodeNames.put(	BNE 	,	"	BNE 	");
                opcodeNames.put(	BLT 	,	"	BLT 	");
                opcodeNames.put(	BGE 	,	"	BGE 	");
                opcodeNames.put(	BLE 	,	"	BLE 	");
                opcodeNames.put(	BGT 	,	"	BGT 	");
                opcodeNames.put(	BSR 	,	"	BSR 	");
                opcodeNames.put(	JSR 	,	"	JSR 	");
                opcodeNames.put(	RET 	,	"	RET 	");
                opcodeNames.put(	RDD 	,	"	RDD 	");
                opcodeNames.put(	WRD 	,	"	WRD 	");
                opcodeNames.put(	WRH 	,	"	WRH 	");
                opcodeNames.put(	WRL 	,	"	WRL 	");
                opcodeNames.put(	ERR 	,	"	ERR 	");

        }
        public static String getOperationName(Integer op) {
                return opcodeNames.get(op);
        }
    }


