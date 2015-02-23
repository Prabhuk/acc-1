package com.acc.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rumpy on 14-01-2015.
 */
public class Condition {
    private static Map<Integer, Integer> conditionsVsNegations = new HashMap<Integer, Integer>();

    static {
        conditionsVsNegations.put(OperationCode.beq, OperationCode.bne);
        conditionsVsNegations.put(OperationCode.blt, OperationCode.bge);
        conditionsVsNegations.put(OperationCode.bgt, OperationCode.ble);
        conditionsVsNegations.put(OperationCode.ble, OperationCode.bgt);
        conditionsVsNegations.put(OperationCode.bge, OperationCode.blt);
        conditionsVsNegations.put(OperationCode.bne, OperationCode.beq);
    }

    private final Integer op;

    public Condition(int op) {
        this.op = op;
    }

    public Integer getOp() {
        return op;
    }

    public static Condition getNegated(Condition cond) {
        return new Condition(conditionsVsNegations.get(cond.getOp()));
    }

    public static Integer getNegatedInstruction(Condition cond) {
        return new Condition(conditionsVsNegations.get(cond.getOp())).getOp();
    }

}
