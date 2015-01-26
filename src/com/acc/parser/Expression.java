package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Operator;
import com.acc.data.Token;
import com.acc.data.TokenType;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Expression {

    private Code code;

    public Expression(Code code) {
        this.code = code;
    }

    //$TODO$ make this return number instead of
    public void parse(Tokenizer tokenizer) {
        final Term term = new Term(code, tokenizer);
        term.parse();
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();
            Operator nextOperator;
            while (next.getTokenType() == TokenType.OPERATOR) {
                nextOperator = (Operator) next;
                if (nextOperator.value().isPlus()) {

//                    result = result + term.parse();
                } else if (nextOperator.value().isMinus()) {
//                    result = result - term.parse();
                } else {
                    break;
                }
            }
        }
    }
}
