package com.acc.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rumpy on 14-01-2015.
 */
public class Condition {
    private static Map<Integer, Integer> conditionsVsNegations = new HashMap<Integer, Integer>();

    static {
        conditionsVsNegations.put(OperationCode.BEQ, OperationCode.BNE);
        conditionsVsNegations.put(OperationCode.BLT, OperationCode.BGE);
        conditionsVsNegations.put(OperationCode.BGT, OperationCode.BLE);
        conditionsVsNegations.put(OperationCode.BLE, OperationCode.BGT);
        conditionsVsNegations.put(OperationCode.BGE, OperationCode.BLT);
        conditionsVsNegations.put(OperationCode.BNE, OperationCode.BEQ);
    }

    private final Integer instruction;

    Condition(int instruction) {
        this.instruction = instruction;
    }

    public Integer getInstruction() {
        return instruction;
    }

    public static Condition getNegated(Condition cond) {
        return new Condition(conditionsVsNegations.get(cond.getInstruction()));
    }

    public static Integer getNegatedInstruction(Condition cond) {
        return new Condition(conditionsVsNegations.get(cond.getInstruction())).getInstruction();
    }

}
