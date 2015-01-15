package com.acc.data;

/**
 * Created by Rumpy on 14-01-2015.
 */
public class Result {
    private int kind;
    private int regno;
    private int value;   //if constant
    private int address; //if variable
    private int cond;    //Conditions: ET, LT, LE, GE, NE, EQ
    private int fixuploc;  //Contains information on where I branch off from. ie: pcBranch

    public Result(int kind, int regno, int value, int address, int cond, int fixuploc) {
        this.kind = kind;
        this.regno = regno;
        this.value = value;
        this.address = address;
        this.cond = cond;
        this.fixuploc = fixuploc;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getRegno() {
        return regno;
    }

    public void setRegno(int regno) {
        this.regno = regno;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getCond() {
        return cond;
    }

    public void setCond(int cond) {
        this.cond = cond;
    }

    public int getFixuploc() {
        return fixuploc;
    }

    public void setFixuploc(int fixuploc) {
        this.fixuploc = fixuploc;
    }
}
