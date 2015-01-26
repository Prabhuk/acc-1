package com.acc.data;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Constant extends Token {

    //Assuming the input code does not contain floating values
    private int value;

    public Constant(String token) {
        super(token);
        value = Integer.parseInt(token, 10);
    }

    @Override
    public TokenType tokenType() {
        return TokenType.CONSTANT;
    }

    public int value() {
        return value;
    }

    public void value(int value) {
        this.value = value;
    }
}
