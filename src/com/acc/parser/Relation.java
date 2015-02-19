package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.RelationalOperator;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.memory.RegisterAllocator;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 04-02-2015.
 */
public class Relation extends Parser {
    public Relation(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = new Expression(code, tokenizer).parse();
        AuxiliaryFunctions.load(code, x);

        Token next = tokenizer.next();
        x.setCondition(new RelationalOperator(next.getToken()).value());


        Result y = new Expression(code, tokenizer).parse();
        AuxiliaryFunctions.load(code, y);

        final int registerNumber = RegisterAllocator.allocateReg();
        AuxiliaryFunctions.putF2(code, OperationCode.CMP, registerNumber, x.regNo(), y.regNo()); //$TODO$ 5 should become a proper register address for the result
        //$TODO$ code should be generated to load expressions for x & y

        x.regNo(registerNumber);
        return x;
    }
}
