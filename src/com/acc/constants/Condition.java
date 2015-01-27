package com.acc.constants;

/**
 * Created by Rumpy on 14-01-2015.
 */
public enum Condition {
    EQ("NE"), LT("GE"), GT("LE"), LE("GT"), GE("LT"), NE("EQ"), NONE("none");
    private final String value;

    Condition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Condition getNegated(Condition cond) {
        return Condition.valueOf(cond.getValue());
    }

}
