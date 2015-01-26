package com.acc.data;

/**
 * Created by Rumpy on 14-01-2015.
 */
public class Result {
    private Kind kind;
    private int regno;
    private int value;   //if constant
    private int address; //if variable
    private Condition cond;   //Conditions: ET, LT, LE, GE, NE, EQ
    private int fixupLoc;  //Contains information on where I branch off from. ie: pcBranch

    public Result(Kind kind, int regno, int value, int address, Condition cond, int fixupLoc) {
        this.kind = kind;
        this.regno = regno;
        this.value = value;
        this.address = address;
        this.cond = cond;
        this.fixupLoc = fixupLoc;
    }

    public Kind kind() {
        return kind;
    }

    public void kind(Kind kind) {
        this.kind = kind;
    }

    public int regNo() {
        return regno;
    }

    public void regNo(int regno) {
        this.regno = regno;
    }

    public int value() {
        return value;
    }

    public void value(int value) {
        this.value = value;
    }

    public int address() {
        return address;
    }

    public void address(int address) {
        this.address = address;
    }

    public Condition condition() {
        return cond;
    }

    public void condition(Condition cond) {
        this.cond = cond;
    }

    public int fixupLoc() {
        return fixupLoc;
    }

    public void fixupLoc(int fixuploc) {
        this.fixupLoc = fixuploc;
    }

}
