package com.acc.structure;

/**
 * Created by prabhuk on 2/17/2015.
 */
public class Register {
    private boolean isAvailable = true;
    private Integer value;

    public Register() {
    }

    public Register(boolean isAvailable, Integer value) {
        this.isAvailable = isAvailable;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
