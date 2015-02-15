package com.acc.data;

import com.acc.constants.Condition;
import com.acc.constants.Kind;

import java.util.List;

/**
 * Created by Rumpy on 14-01-2015.
 */
public class Result {
    private Kind kind;
    private Integer regno;
    private Integer value;   //if constant
    private Integer address; //if variable
    private String variableName; //if variable. This might be merged with address later. $TODO$
    private Condition cond;   //Conditions: ET, LT, LE, GE, NE, EQ
    private Integer fixupLoc;  //Contains information on where I branch off from. ie: pcBranch
    private List<Integer> dimensions; //Contains the dimensions for array initialization $TODO$ Add array initialization to the symbol table

    public Result(Kind kind, Integer regno, Integer value, Integer address, Condition cond, Integer fixupLoc, List<Integer> dimensions) {
        this.kind = kind;
        this.regno = regno;
        this.value = value;
        this.address = address;
        this.cond = cond;
        this.fixupLoc = fixupLoc;
        this.dimensions = dimensions;
    }

    public Result(Kind kind, Integer regno, Integer value, Integer address, Condition cond, Integer fixupLoc) {
        this(kind, regno, value, address, cond, fixupLoc, null);
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Kind kind() {
        return kind;
    }

    public void kind(Kind kind) {
        this.kind = kind;
    }

    public Integer regNo() {
        return regno;
    }

    public void regNo(Integer regno) {
        this.regno = regno;
    }

    public Integer value() {
        return value;
    }

    public void value(Integer value) {
        this.value = value;
    }

    public Integer address() {
        return address;
    }

    public void address(Integer address) {
        this.address = address;
    }

    public Condition condition() {
        return cond;
    }

    public void condition(Condition cond) {
        this.cond = cond;
    }

    public Integer fixupLoc() {
        return fixupLoc;
    }

    public void setCondition(Condition cond)
    {
        this.cond=cond;
    }

    public void fixupLoc(Integer fixuploc) {
        this.fixupLoc = fixuploc;
    }

    public List<Integer> dimensions() {
        return dimensions;
    }

}
