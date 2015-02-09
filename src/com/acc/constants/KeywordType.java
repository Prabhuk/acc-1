package com.acc.constants;

/**
 * Created by prabhuk on 1/26/2015.
 */
public enum KeywordType {
    IF("if"),
    LET("let"),
    FI("fi"),
    CALL("call"),
    THEN("then"),
    ELSE("else"),
    WHILE("while"),
    DO("do"),
    OD("od"),
    RETURN("return"),
    VAR("var"),
    ARRAY("array"),
    FUNCTION("function"),
    PROCEDURE("procedure"),
    MAIN("main");
    private final String value;

    KeywordType(String value) {
        this.value = value;
    }

    public static KeywordType isKeyword(String s) {
        for (KeywordType keyword : values()) {
            if (keyword.getValue().equals(s)) {
                return keyword;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public boolean isIf() {
        return IF == this;
    }

    public boolean isLet() {
        return LET == this;
    }

    public boolean isFi() {
        return FI == this;
    }

    public boolean isCall() {
        return CALL == this;
    }

    public boolean isThen() {
        return THEN == this;
    }

    public boolean isElse() {
        return ELSE == this;
    }

    public boolean isWhile() {
        return WHILE == this;
    }

    public boolean isDo() {
        return DO == this;
    }

    public boolean isOd() {
        return OD == this;
    }

    public boolean isReturn() {
        return RETURN == this;
    }

    public boolean isVar() {
        return VAR == this;
    }

    public boolean isArray() {
        return ARRAY == this;
    }

    public boolean isFunction() {
        return FUNCTION == this;
    }

    public boolean isProcedure() {
        return PROCEDURE == this;
    }

    public boolean isMain() {
        return MAIN == this;
    }

    public boolean isStatementBeginKeyword() {
        return this.isLet() || this.isCall()
                || this.isIf() || this.isWhile() || this.isReturn();
    }


}
