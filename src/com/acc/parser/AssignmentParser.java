package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.List;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class AssignmentParser extends Parser {
    public AssignmentParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        final Token lhs = tokenizer.next();

        if (!lhs.isDesignator()) {
            throw new SyntaxErrorException("Designator expected. Found[" + lhs.getToken() + "] instead");
        }
        final Symbol recent = getSymbolTable().getRecentOccurence(lhs.getToken());
        List<Result> arrayIdentifiers = accumulateArrayIdentifiers(recent);
        boolean lhsIsArrayIdentifier = !arrayIdentifiers.isEmpty();
        //$TODO$ well do something with the identifiers
        handleAssignmentOperator();
        Result y = new Expression(code, tokenizer).parse();

        if(!lhsIsArrayIdentifier && !y.kind().isArray()) {
            final Result x = new Result(Kind.VAR);
            x.setVariableName(lhs.getToken());
            AuxiliaryFunctions.addMoveInstruction(code, x, y, getSymbolTable());
            return x;
        }

        if(lhsIsArrayIdentifier && y.kind().isArray() && arrayIdentifiers.size() == 0) {
            //This case will never be reached due to accumulateArrayIdentifiers throwing error on missing [
            //Handle this case gracefully
            throw new SyntaxErrorException("Assignment rhs unhandled type ["+ y.kind().name() +"]");
//            return x;

        }

        Result x;
        if (lhsIsArrayIdentifier) {
            createAddA(lhs.getToken(), arrayIdentifiers);
            x = new Result(Kind.INTERMEDIATE);
            x.setIntermediateLoation(code.getPc() - 1);
            Result storeInstruction;
            if(y.kind().isArray()) {
                loadYarray(y.getVariableName());
                storeInstruction = new Result(Kind.INTERMEDIATE);
                storeInstruction.setIntermediateLoation(code.getPc() - 1);
            } else {
                storeInstruction = y;
            }
            AuxiliaryFunctions.addInstruction(OperationCode.store, code, x, storeInstruction, getSymbolTable());
            AuxiliaryFunctions.addKillInstruction(code, recent);
        } else {
            x = new Result(Kind.VAR);
            x.setVariableName(lhs.getToken());
            Result moveInstruction;
            if(y.kind().isArray()) {
                loadYarray(y.getVariableName());
                moveInstruction = new Result(Kind.INTERMEDIATE);
                moveInstruction.setIntermediateLoation(code.getPc() - 1);
            } else {
                moveInstruction = y;
            }
            AuxiliaryFunctions.addMoveInstruction(code, x, moveInstruction, getSymbolTable());
        }

        return x;
    }

    private void loadYarray(String tokenName) {
        final Symbol recent = getSymbolTable().getRecentOccurence(tokenName);
        List<Result> arrayIdentifiers = accumulateArrayIdentifiers(recent);
        if(arrayIdentifiers.size() > 0) {
            createAddA(tokenName, arrayIdentifiers);
            final Result loadInstruction = new Result(Kind.INTERMEDIATE);
            loadInstruction.setIntermediateLoation(code.getPc() - 1);
            AuxiliaryFunctions.addInstruction(OperationCode.load, code, loadInstruction, null, getSymbolTable());
            //Move this to tokenName
        } else {
            throw new SyntaxErrorException("Assignment of complete arrays are not supported");
        }
    }

    private void loadY(Result y, Result x) {
        AuxiliaryFunctions.addInstruction(OperationCode.load, code, y, null, getSymbolTable());
        final Result moveInstruction = new Result(Kind.INTERMEDIATE);
        moveInstruction.setIntermediateLoation(code.getPc() - 1);
        AuxiliaryFunctions.addMoveInstruction(code, x, moveInstruction, getSymbolTable());
    }

    private void createAddA(String tokenName, List<Result> arrayIdentifiers) {
        if(arrayIdentifiers.size() == 0) {
            return;
        }
        Result previous = null;
        Result previousSumComponent = null;

        final Symbol declaration = getSymbolTable().getDeclaration(tokenName);
        final List<Result> originalIdentifiers = declaration.getArrayIdentifiers();

        for (int i=0; i<arrayIdentifiers.size(); i++) {
            previous = arrayIdentifiers.get(i);
            for(int j=i+1; j<originalIdentifiers.size(); j++) {
                final Result originalIdentifier = originalIdentifiers.get(j);
                AuxiliaryFunctions.addInstruction(OperationCode.mul, code, previous, originalIdentifier, getSymbolTable());
                previous = new Result(Kind.INTERMEDIATE);;
                previous.setIntermediateLoation(code.getPc() - 1);
            }
            if(previousSumComponent != null) {
                AuxiliaryFunctions.addInstruction(OperationCode.add, code, previous, previousSumComponent, getSymbolTable());
                previous = new Result(Kind.INTERMEDIATE);;
                previous.setIntermediateLoation(code.getPc() - 1);
            }
            previousSumComponent = previous;
        }





        final Result intSize = new Result(Kind.CONSTANT);
        intSize.value(4);
        AuxiliaryFunctions.addInstruction(OperationCode.mul, code, previous, intSize, getSymbolTable());
        final Result mulInstruction = new Result(Kind.INTERMEDIATE);
        mulInstruction.setIntermediateLoation(code.getPc() - 1);

//        final Symbol lhsSymbol = getSymbolTable().getRecentOccurence(tokenName);
//        final int lhsBaseAddress = getSymbolTable().getOffset(lhsSymbol);
        final Result lBaseAddr = new Result(Kind.BASE_ADDRESS);
        lBaseAddr.setVariableName(tokenName);

        final Result addInstruction = new Result(Kind.INTERMEDIATE);
        addInstruction.setIntermediateLoation(code.getPc());
        AuxiliaryFunctions.addInstruction(OperationCode.add, code, FP, lBaseAddr, getSymbolTable());
        AuxiliaryFunctions.addInstruction(OperationCode.adda, code, mulInstruction, addInstruction, getSymbolTable());
    }

    private Result calculateMulInstructionValue(List<Result> arrayIdentifiers) {
        Result previous = arrayIdentifiers.get(0);
        for (int i=1; i<arrayIdentifiers.size(); i++) {
            final Result arrayIdentifier = arrayIdentifiers.get(i);
            AuxiliaryFunctions.addInstruction(OperationCode.mul, code, arrayIdentifier, previous, getSymbolTable());
            previous = new Result(Kind.INTERMEDIATE);;
            previous.setIntermediateLoation(code.getPc());
        }
        return previous;
    }

    private void handleAssignmentOperator() {
        final Token assignmentOp = tokenizer.next();
        if (!assignmentOp.isAssignmentOperator()) {
            throw new SyntaxErrorException("Assignment operator [<-] expected. Found[" + assignmentOp.getToken() + "] instead");
        }
    }


}
