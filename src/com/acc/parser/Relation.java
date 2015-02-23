package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.constants.SSAOpCodes;
import com.acc.data.*;
import com.acc.memory.RegisterAllocator;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolType;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 04-02-2015.
 */
public class Relation extends Parser {

    public Relation(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {

        Result x = new Expression(code, tokenizer, ssaCode).parse();

        final Symbol symbol;
        if(x.kind().isVariable()) {
            symbol = new Symbol(x.getVariableName(), code.getPc(), SymbolType.VARIABLE, true, x.address());
        } else {
            if(x.kind().isConstant()) {
                symbol = new Symbol(String.valueOf(x.value()), code.getPc(), null, true, x.value());
            } else {
                symbol = new Symbol("R" + String.valueOf(x.value()), code.getPc(), null, true, x.regNo());
            }
        }
        AuxiliaryFunctions.load(code, x);

        Token next = tokenizer.next();
        x.setCondition(new RelationalOperator(next.getToken()).value());


        Result y = new Expression(code, tokenizer, ssaCode).parse();
        symbol.setResult(y);
        AuxiliaryFunctions.generateSSA(ssaCode, SSAOpCodes.cmp, x, y);
        AuxiliaryFunctions.load(code, y);

        final int registerNumber = RegisterAllocator.allocateReg();
        AuxiliaryFunctions.putF2(code, OperationCode.CMP, registerNumber, x.regNo(), y.regNo(), symbol); //$TODO$ 5 should become a proper register address for the result
        //$TODO$ code should be generated to load expressions for x & y

        x.regNo(registerNumber);
        return x;
    }
}
