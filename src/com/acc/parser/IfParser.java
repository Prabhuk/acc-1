package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.Code;
import com.acc.data.Keyword;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.InvalidTokenException;
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
        Result x = new Relation(code, tokenizer).parse();
        AuxiliaryFunctions.CJF(code, x);
        final Token next = tokenizer.next();
        if (!next.tokenType().isKeyword() || !((Keyword) next).type().isIf()) {
            throw new InvalidTokenException("[then] token missing. Found [" + next.getToken() + "]");
        }

        new StatSequence(code, tokenizer).parse(); //Ignoring the return type. Shouldn't mean anything at this context.
        Result follow = new Result(Kind.FIXUP_DUMMY, 0, 0, 0, null, ZERO);

        Token incoming = tokenizer.next();
        Token lookingAheadToken = tokenizer.next();
        while (elseIf(incoming, lookingAheadToken)) {
            AuxiliaryFunctions.FJLink(code, follow);
            code.Fixup(x.fixupLoc());
            x = parse();
            AuxiliaryFunctions.CJF(code, x);

            new StatSequence(code, tokenizer).parse();
            incoming = tokenizer.next();
            lookingAheadToken = tokenizer.next();
        }
        tokenizer.previous(); //compensating the looking ahead token

        if (incoming.tokenType().isKeyword() && ((Keyword) incoming).type().isElse()) { //The dangling else
            AuxiliaryFunctions.FJLink(code, follow);
            code.Fixup(x.fixupLoc());
            new StatSequence(code, tokenizer).parse();
        } else {
            code.Fixup(x.fixupLoc());
        }
        code.Fixlink(follow);

        final Token finalFiToken = tokenizer.next();
        if (!finalFiToken.tokenType().isKeyword() || !((Keyword) finalFiToken).type().isFi()) {
            throw new InvalidTokenException("[If block] must end with [fi] token. Found [" + finalFiToken.getToken() + "]");
        }
        return x;
    }

    private boolean elseIf(Token incoming, Token lookingAheadForIf) {
        return incoming.tokenType().isKeyword() && ((Keyword) incoming).type().isElse()
                && lookingAheadForIf.tokenType().isKeyword() && ((Keyword) incoming).type().isIf();
    }

}
