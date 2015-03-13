package com.acc.data;

/**
 * Created by prabhuk on 3/6/2015.
 */
public enum BlockType {
    WHILE_BODY,
    WHILE_HEAD,
    WHILE_FOLLOW,
    CALL,
    IF,
    COMPARE,
    IF_JOIN;

    public boolean isIF() {
        return this == IF;
    }

    public boolean isWhileBody() {
        return this == WHILE_BODY;
    }
    public boolean isWhileHead() {
        return this == WHILE_HEAD;
    }

    public boolean isWhileFollow() {
        return this == WHILE_FOLLOW;
    }
    public boolean isCall() {
        return this == CALL;
    }
    public boolean isCompare() {
        return this == COMPARE;
    }
}
