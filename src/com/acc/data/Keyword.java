package com.acc.data;

import com.acc.constants.KeywordType;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Keyword extends Token {

    private KeywordType type;

    public Keyword(String token, KeywordType type) {
        super(token);
        this.type = type;
    }

    @Override
    public TokenType tokenType() {
        return TokenType.KEYWORD;
    }

    public KeywordType type() {
        return type;
    }

    public boolean isIf() {
        return KeywordType.IF == type;
    }

    public boolean isLet() {
        return KeywordType.LET == type;
    }

    public boolean isFi() {
        return KeywordType.FI == type;
    }

    public boolean isCall() {
        return KeywordType.CALL == type;
    }

    public boolean isThen() {
        return KeywordType.THEN == type;
    }

    public boolean isElse() {
        return KeywordType.ELSE == type;
    }

    public boolean isWhile() {
        return KeywordType.WHILE == type;
    }

    public boolean isDo() {
        return KeywordType.DO == type;
    }

    public boolean isOd() {
        return KeywordType.OD == type;
    }

    public boolean isReturn() {
        return KeywordType.RETURN == type;
    }

    public boolean isVar() {
        return KeywordType.VAR == type;
    }

    public boolean isArray() {
        return KeywordType.ARRAY == type;
    }

    public boolean isFunction() {
        return KeywordType.FUNCTION == type;
    }

    public boolean isProcedure() {
        return KeywordType.PROCEDURE == type;
    }

    public boolean isMain() {
        return KeywordType.MAIN == type;
    }

    public boolean isStatementBeginKeyword() {
        return type.isLet() || type.isCall()
                || type.isIf() || type.isWhile() || type.isReturn();
    }


}
