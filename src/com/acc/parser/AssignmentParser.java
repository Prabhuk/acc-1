package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.List;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class AssignmentParser extends Parser {
    public AssignmentParser(Code code, Tokenizer tokenizer, SymbolTable symbolTable) {
        super(code, tokenizer, symbolTable);
    }

    @Override
    public Result parse() {
        final Result lhs = new Expression(code, tokenizer, symbolTable).parse();
        if (!lhs.isArray() && !lhs.isVariable()) {
            throw new SyntaxErrorException("Designator expected. Found[" + lhs.kind().name() + "] instead");
        }
        //$TODO$ well do something with the identifiers
        handleAssignmentOperator();
        final Symbol recentLHS = symbolTable.getRecentOccurence(lhs.getVariableName());
        final Computation mainProgram = this.getOutputContents().getMainProgram();
        if(code.getProgramName() != null && recentLHS.isGlobal() && !mainProgram.getProgramName().equals(code.getProgramName())) {
            AuxiliaryFunctions.addKillInstruction(mainProgram.getCode(), recentLHS);
            //$TODO$ added in the right place?
        }

        Result y = new Expression(code, tokenizer, symbolTable).parse();
        if(y.isVariable()) {
            final Symbol recentRHS = symbolTable.getRecentOccurence(y.getVariableName());
            if(recentRHS != null) {
                y.setLocation(recentRHS.getSuffix());
            }
        }


        if(!lhs.isArray() && !y.isArray()) {
            final Result x = new Result(Kind.VAR);
            x.setVariableName(lhs.getVariableName());
            AuxiliaryFunctions.addMoveInstruction(code, x, y, getSymbolTable());
            return x;
        }

        if(lhs.isArray() && y.isArray() && lhs.getArrayIdentifiers().size() == 0) {
            //This case will never be reached due to accumulateArrayIdentifiers throwing error on missing [
            // $TODO$ do we need to check a <- b case where a and b both are arrays?
            //Handle this case gracefully
            throw new SyntaxErrorException("Assignment rhs unhandled type ["+ y.kind().name() +"]");
//            return x;

        }

        Result x;
        if (lhs.isArray()) {
            createAddA(lhs.getVariableName(), lhs.getArrayIdentifiers());
            x = new Result(Kind.INTERMEDIATE);
            x.setIntermediateLoation(code.getPc() - 1);
            Result storeInstruction;
            if(y.isArray()) {
                loadYarray(y);
                storeInstruction = new Result(Kind.INTERMEDIATE);
                storeInstruction.setIntermediateLoation(code.getPc() - 1);
            } else {
                storeInstruction = y;
            }
            AuxiliaryFunctions.addInstruction(OperationCode.store, code, x, storeInstruction, getSymbolTable());
            Symbol recent = getSymbolTable().getRecentOccurence(lhs.getVariableName());
            AuxiliaryFunctions.addKillInstruction(code, recent);
        } else {
            x = new Result(Kind.VAR);
            x.setVariableName(lhs.getVariableName());
            Result moveInstruction;
            if(y.isArray()) {
                loadYarray(y);
                moveInstruction = new Result(Kind.INTERMEDIATE);
                moveInstruction.setIntermediateLoation(code.getPc() - 1);
            } else {
                moveInstruction = y;
            }
            AuxiliaryFunctions.addMoveInstruction(code, x, moveInstruction, getSymbolTable());
        }

        return x;
    }

    private void loadYarray(Result y) {
        String tokenName = y.getVariableName();
        List<Result> arrayIdentifiers = y.getArrayIdentifiers();
        if(arrayIdentifiers != null && arrayIdentifiers.size() > 0) {
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
