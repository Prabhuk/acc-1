package com.acc.parser;

import com.acc.data.*;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Expression {

    public static int parse(Tokenizer tokenizer) {
        int result = Term.parse(tokenizer);
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();
            Operator nextOperator;
            while (next.getTokenType() == TokenType.OPERATOR) {
                nextOperator = (Operator) next;
                if(nextOperator.value().isPlus()) {
                    result = result + Term.parse(tokenizer);
                } else if(nextOperator.value().isMinus()) {
                    result = result - Term.parse(tokenizer);
                } else {
                    break;
                }
            }
        }
        return result;
    }
}
