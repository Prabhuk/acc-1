package com.acc.parser;

import com.acc.constants.KeywordType;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.BasicBlock;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

import java.util.Set;

/**
 * Created by prabhuk on 1/26/2015.
 */
public class WhileParser extends Parser {
    private BasicBlock loopBlock;

    public WhileParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);

    }

    @Override
    public Result parse() {
        final int loop = code.getPc();
        loopBlock = code.getCurrentBlock();
        Result x = new Relation(code, tokenizer).parse();
        AuxiliaryFunctions.CJF(code, x);

        final BasicBlock currentBlock = code.getCurrentBlock();
        final Set<BasicBlock> parents = currentBlock.getParents();
        if(parents.size() != 1) {
            throw new RuntimeException("There should be only one parent for the join block at this time");
        }
        BasicBlock parent = null;
        for (BasicBlock p : parents) {
            parent = p;
            break;
        }

        BasicBlock join = currentBlock;
        join.setLeft(parent);
        x.setJoin(join);

        currentBlock.setJoinBlock(join); //basically self reference

        final Token next = handleDoToken();

        final BasicBlock right = new BasicBlock();
        join.setRight(right);
        final Result rightTree = new StatSequence(code, tokenizer).parse();
        if(rightTree.getJoin() != null) {
            x.setJoin(rightTree.getJoin());
        }
        AuxiliaryFunctions.BJ(code, loop, loopBlock); //Backward Jump to the loop beginning.
        AuxiliaryFunctions.createPhiInstructions(getSymbolTable(), join, code);
        code.setCurrentBlock(new BasicBlock());
        handleODtoken(next);
        return x; //$TODO$ Do we need x at all?
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
