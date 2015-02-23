package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.constants.SSAOpCodes;
import com.acc.data.*;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Expression extends Parser {

    public Expression(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        Result x, y;
        x = new Term(code, tokenizer, ssaCode).parse();
        Token next = tokenizer.next();
        Operator nextOperator;
        while (next.isOperator() && (((Operator) next).value().isPlus() || ((Operator) next).value().isMinus())) {
            nextOperator = ((Operator) next);
            int instructionCode;
            String ssaInstruction;
            if (nextOperator.value().isPlus()) {
                instructionCode = OperationCode.ADD;
                ssaInstruction = SSAOpCodes.add;
            } else { //Minus
                instructionCode = OperationCode.SUB;
                ssaInstruction = SSAOpCodes.sub;
            }
            y = new Term(code, tokenizer, ssaCode).parse();
            AuxiliaryFunctions.generateSSA(ssaCode, ssaInstruction, x, y);
            AuxiliaryFunctions.combine(code, instructionCode, x, y);
            next = tokenizer.next();
        }
//        if(x.kind().isVariable()) {
//            x.address(getSymbolTable().getRecentOccurence(x.getVariableName())
//        }
        tokenizer.previous(); //Rolling back the token which broke the while loop
        return x;
    }
}
