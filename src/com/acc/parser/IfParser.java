package com.acc.parser;

import com.acc.constants.KeywordType;
import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.structure.BasicBlock;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;


/**
 * Created by prabhuk on 1/26/2015.
 */
public class IfParser extends Parser {

    public static final Integer ZERO = 0;

    public IfParser(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = new Relation(code, tokenizer).parse();  //Statement eats the first word for all statements except assignment
        AuxiliaryFunctions.CJF(code, x);
        final BasicBlock currentBlock = code.getCurrentBlock();
        BasicBlock localCurrentBlock = currentBlock;

        BasicBlock join = new BasicBlock();
        x.setJoin(join);

        currentBlock.setJoinBlock(join);

        final BasicBlock left = new BasicBlock();
        join.setLeft(left);
        localCurrentBlock.addChild(left);

        code.setCurrentBlock(left);

        handleThenToken();

        final Result leftTree = new StatSequence(code, tokenizer).parse();//Ignoring the return type. Shouldn't mean anything at this context.
        if (leftTree.getJoin() != null) {
            join.setLeft(leftTree.getJoin());
        }

        Result follow = new Result(Kind.FIXUP_DUMMY, 0, 0, 0, null, ZERO);
        Token incoming = tokenizer.next();
        if (isElse(incoming)) {
            final BasicBlock right = new BasicBlock();
            join.setRight(right);
            localCurrentBlock.addChild(right);
            code.setCurrentBlock(right);
            AuxiliaryFunctions.FJLink(code, follow);
            code.Fixup(x.fixupLoc());
            final Result rightTree = new StatSequence(code, tokenizer).parse();
            if (rightTree.getJoin() != null) {
                join.setRight(rightTree.getJoin());
            }
        } else {
            tokenizer.previous();
            join.setRight(currentBlock);
            code.Fixup(x.fixupLoc());
        }
        code.Fixlink(follow);
        AuxiliaryFunctions.createPhiInstructions(getSymbolTable(), join, code);
        handleFiToken();
        if(join.getLeft() != null) {
            join.getLeft().addChild(join);
        }
        if(join.getRight() != null) {
            join.getRight().addChild(join);
        }
        code.setCurrentBlock(join);
        return x;
    }


    private void handleFiToken() {
        final Token finalFiToken = tokenizer.next();
        if (!finalFiToken.isKeyword() || !((Keyword) finalFiToken).isFi()) {
            throw new SyntaxErrorException(KeywordType.FI, finalFiToken);
        }
    }

    private void handleThenToken() {
        final Token next = tokenizer.next();
        if (!next.isKeyword() || !((Keyword) next).isThen()) {
            throw new SyntaxErrorException(KeywordType.THEN, next);
        }
    }

    private boolean isElse(Token incoming) {
        return incoming.isKeyword() && ((Keyword) incoming).isElse();
    }

}
