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
    private int fixuploc;  //Contains information on where I branch off from. ie: pcBranch

    public Result(Kind kind, int regno, int value, int address, Condition cond, int fixuploc) {
        this.kind = kind;
        this.regno = regno;
        this.value = value;
        this.address = address;
        this.cond = cond;
        this.fixuploc = fixuploc;
    }

    public Kind kind() {
        return kind;
    }

    public void kind(Kind kind) {
        this.kind = kind;
    }

    public int getRegno() {
        return regno;
    }

    public void setRegno(int regno) {
        this.regno = regno;
    }

    public int value() {
        return value;
    }

    public void value(int value) {
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public Condition getCond() {
        return cond;
    }

    public void setCond(Condition cond) {
        this.cond = cond;
    }

    public int getFixuploc() {
        return fixuploc;
    }

    public void setFixuploc(int fixuploc) {
        this.fixuploc = fixuploc;
    }

}
