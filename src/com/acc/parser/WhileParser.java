package com.acc.parser;

import com.acc.constants.KeywordType;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.BasicBlock;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.PhiInstructionHelper;
import com.acc.util.Tokenizer;

import java.util.Set;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class WhileParser extends Parser {

    public WhileParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);

    }

    @Override
    public Result parse() {
        final int loop = code.getPc();

        final BasicBlock _currentBlock = code.getCurrentBlock();
        Set<BasicBlock> parents = _currentBlock.getParents();
        if(parents.size() > 1) {
            final BasicBlock nextBlock = new BasicBlock();
            _currentBlock.addChild(nextBlock, true);
            code.setCurrentBlock(nextBlock);
        }

        final BasicBlock currentBlock = code.getCurrentBlock();
        BasicBlock loopBlock = code.getCurrentBlock();
        Result x = new Relation(code, tokenizer).parse();
        AuxiliaryFunctions.CJF(code, x, getSymbolTable());

        parents = currentBlock.getParents();
        BasicBlock parent = null;
        for (BasicBlock p : parents) {
            parent = p;
            break;
        }

        BasicBlock join = currentBlock; //Distinction is necessary to maintain readability of code
        join.setLeft(parent);
        x.setJoin(join);



        currentBlock.setJoinBlock(join); //basically self reference

        handleDoToken();

        final BasicBlock right = new BasicBlock();
        currentBlock.addChild(right, true);
        join.setRight(right);
        code.setCurrentBlock(right);

        final Result rightTree = new StatSequence(code, tokenizer).parse();
        if(rightTree.getJoin() != null) {
            x.setJoin(rightTree.getJoin());
            rightTree.getJoin().addChild(loopBlock); //Does loop body dominate loop condition block?
        } else {
            right.addChild(loopBlock);
        }

        AuxiliaryFunctions.BJ(code, loop); //Backward Jump to the loop beginning.
        PhiInstructionHelper.createPhiInstructions(getSymbolTable(), join, code);

        final BasicBlock nextBlock = new BasicBlock();
        join.addChild(nextBlock, true);

        code.setCurrentBlock(nextBlock);
        handleODtoken(tokenizer.next());

        return x;
    }


    private void handleODtoken(Token next) {
        if (!next.isKeyword() || !((Keyword) next).isOd()) {
            throw new SyntaxErrorException(KeywordType.OD, next);
        }
    }

    private Token handleDoToken() {
        final Token next = tokenizer.next();
        if (!next.isKeyword() || !((Keyword) next).isDo()) {
            throw new SyntaxErrorException(KeywordType.DO, next);
        }
        return next;
    }
}
