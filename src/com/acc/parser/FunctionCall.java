package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.data.TokenType;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.BasicBlock;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/8/2015.
 */
public class FunctionCall extends Parser {

    public FunctionCall(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        final Token procedureName = tokenizer.next();
        if (!procedureName.isIdentifier()) {
            throw new SyntaxErrorException(procedureName.tokenType(), TokenType.IDENTIFIER);
        }
        final BasicBlock functionBlock = new BasicBlock();
        code.getCurrentBlock().addChild(functionBlock, false);
        code.setCurrentBlock(functionBlock);
        final Token openBracket = tokenizer.next();
        List<Result> parameters = new ArrayList<Result>();
        if (openBracket.getToken().equals("(")) {
            //throw new SyntaxErrorException("Excpected \"(\" after procedure name [" + procedureName.getToken() + "]. Found [" + openBracket.getToken() + "] instead");
            //$TODO$ not supported according to the Grammar but call foo; is present in test
            Token lookAhead = tokenizer.next();
            if (!lookAhead.getToken().equals(")")) {
                tokenizer.previous(); //Allowing expression to process the first parameter value
                parameters.add(new Expression(code, tokenizer, symbolTable).parse());
                //$TODO$ parameter could be an array identifier
                lookAhead = tokenizer.next();
                while (lookAhead.getToken().equals(",")) {
                    parameters.add(new Expression(code, tokenizer, symbolTable).parse());
                    lookAhead = tokenizer.next();
                }
                if (!lookAhead.getToken().equals(")")) { //End brackets should've broken the while loop. Otherwise syntax error
                    throw new SyntaxErrorException("Expected [\")\"]. Found [" + lookAhead.getToken() + "] instead");
                }
            }
        } else {
            tokenizer.previous();
        }
        final List<String> args = getArgumentNamesForProcedure(procedureName.getToken());
        if (args.size() != parameters.size()) {
//            throw new SyntaxErrorException("Argument list mismatch in the procedure call for the procedure [" + procedureName.getToken() + "]");
        }

        for (int i = 0; i < parameters.size(); i++) {
            Result parameter = parameters.get(i);
            //$TODO$ somehow pass the parameters to the procdeure program
//            String argumentName = args.get(i);
            if (parameter.isVariable()) {
                Symbol recent = getSymbolTable().getRecentOccurence(parameter.getVariableName());
            }
//            Result x = new Result(recent.getType().isArray() ? Kind.ARRAY : Kind.VAR, null, null, null, null, null, null);
//            List<Result> arrayIdentifiers = accumulateArrayIdentifiers(recent);
            //$TODO$ well do something with the identifiers - Should GENERATE CODE FOR CALLEE

//            AuxiliaryFunctions.addInstruction(OperationCode.move, code, x, parameter, getSymbolTable());
        }


//        final Result x = new Result(Kind.PROCEDURE);
//        x.setVariableName(procedureName.getToken());
//        AuxiliaryFunctions.addInstruction(OperationCode.call, code, x, null, symbolTable);

        final Result x = new Result(Kind.INTERMEDIATE);
        x.setIntermediateLoation(code.getPc());
        Result y = new Result(Kind.PROCEDURE);
        y.setVariableName(procedureName.getToken());
        AuxiliaryFunctions.addInstruction(OperationCode.call, code, y, null, symbolTable);
        final BasicBlock newBlock = new BasicBlock();
        functionBlock.addChild(newBlock, false);
        code.setCurrentBlock(newBlock);

        return x; //$TODO$ return relevant value
    }
}
