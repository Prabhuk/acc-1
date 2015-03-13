package com.acc.vm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prabhuk on 3/12/2015.
 */
public class Register {

    private final Map<Integer, Integer> regInfo;
    public static int DUMMY_REGISTER = 11; //used for instruction results which are never used
    public static int DUMMY_REGISTER_2 = 12; //used for instruction results which are never used
    public static int SCRATCH_REGISTER_1 = 9; //used for instruction results which are never used
    public static int SCRATCH_REGISTER_2 = 10; //used for instruction results which are never used
    public static Map<Integer, Integer> ssaToDLX = new HashMap<Integer, Integer>();


    public static final int FP = 28;
    public static final int SP = 29;
    public static final int RP = 31;

    static {
        ssaToDLX.put(0, 1);
        ssaToDLX.put(1, 2);
        ssaToDLX.put(2, 3);
        ssaToDLX.put(3, 4);
        ssaToDLX.put(4, 5);
        ssaToDLX.put(5, 6);
        ssaToDLX.put(6, 7);
        ssaToDLX.put(7, 8);
    }

    public Register(Map<Integer, Integer> regInfo) {
        this.regInfo = regInfo;
    }

    public int getReg4Intermediates(Integer intermediateLocation) {
        if(regInfo.get(intermediateLocation) != null) {
            final Integer ssaRegister = regInfo.get(intermediateLocation);
            final Integer dlxReg = ssaToDLX.get(ssaRegister);
            if(dlxReg != null) {
                return dlxReg;
            }
            return SCRATCH_REGISTER_1;
        }
        return DUMMY_REGISTER;
    }

    public int getDLXReg(Integer register) {
        if(ssaToDLX.get(register) != null) {
            return ssaToDLX.get(register);
        }
        return SCRATCH_REGISTER_1;
    }

    public boolean isScratch(int regNo) {
        return regNo == SCRATCH_REGISTER_1 || regNo == SCRATCH_REGISTER_2;
    }

    public int getFP() {
        return FP;
    }

    public int getSP() {
        return SP;
    }

    public int getRP() {
        return RP;
    }
}
