package com.acc.util;

import com.acc.constants.KeywordType;
import com.acc.data.*;
import com.acc.exception.UnknownOperatorException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Rumpy on 15-01-2015.
 * <p/>
 * The assignment operator <- has been converted to ` in getTokens method
 * The "(" and ")" bracket are always defined as operators in this class
 */
public class Tokenizer {
    private String input;
    private Map<Integer, Token> tokenSet;
    private int currentPointer = 0;
    private int tokenCounter = 0;
    private static boolean movedBack = false;


    public Tokenizer(String filePath) throws IOException {
        File sourceFile = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(sourceFile));
            char[] chars = new char[(int) sourceFile.length()];
            reader.read(chars);
            input = new String(chars);
            input = input.trim();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        tokenSet = new LinkedHashMap<Integer, Token>();
        //input = input.replaceAll("\n", " ");
        //input = input.replaceAll("\t", " ");
        //input = input.replaceAll("<-", "`");
        //input = input.replaceAll("//", "~");
        //st = new StringTokenizer(input, ", <>={}()`;", true);
    }

    /**
     * For parsing that requires look ahead, we allow one token look ahead.
     * Calling the previous multiple times without moving forward (next()) will result in an error.
     */
    public void previous() {
        if (movedBack) {
            throw new UnknownOperatorException("Cannot move back more than once at a time. ");
        }
        movedBack = true;
    }

    public Token next() {
        if (movedBack && !tokenSet.isEmpty()) {
            movedBack = false;
            return tokenSet.get(tokenCounter);
        }

        if (!hasNext()) {
            throw new UnsupportedOperationException("You should call next only when hasNext is true");
        }

        StringBuilder token = new StringBuilder();
        char currentChar = eliminateEmptySpaces();
        if (currentChar == '.') {
            Printer.print("DEBUG: currentPointer [" + currentPointer + "] inputLength [" + input.length() + "]");
            currentPointer++;
            Separator s = new Separator(token.append(currentChar).toString());
            tokenSet.put(++tokenCounter, s);
            return s;
        }

        if (Character.isDigit(currentChar)) {
            return handleConstants(token, currentChar);
        } else if (Character.isAlphabetic(currentChar)) {
            return handleIdentifiersAndKeywords(token, currentChar);
        } else if (isSeparatorCharacter(currentChar)) {
            currentPointer++;
            Separator s = new Separator(token.append(currentChar).toString());
            tokenSet.put(++tokenCounter, s);
            return s;
        } else if (isRelationalOperator(currentChar)) {
            char previous = currentChar;
            token.append(currentChar);
            currentPointer++;
            currentChar = input.charAt(currentPointer);
            if (isAssignmentOperator(currentChar, previous)) {
                token.append(currentChar);
                currentPointer++;
                AssignmentOperator a = new AssignmentOperator(token.toString());
                tokenSet.put(++tokenCounter, a);
                return a;
            } else if (currentChar == '=') {
                token.append(input.charAt(currentPointer++));
            }
            RelOp ro = new RelOp(token.toString());
            tokenSet.put(++tokenCounter, ro);
            return ro;
        } else if (isOperatorCharacter(currentChar)) {
            char temp = currentChar;
            token.append(temp);
            currentPointer++;
            currentChar = input.charAt(currentPointer);
            if (isComment(currentChar, temp)) {
                eatCommentText(currentChar);
                return next();
            }
            Operator o = new Operator(token.toString());
            tokenSet.put(++tokenCounter, o);
            return o;
        } else if (isCommentCharacter(currentChar)) {
            eatCommentText(currentChar);
            return next();
        } else {
            currentPointer++;
            throw new RuntimeException("Unrecognized character [" + currentChar + "]");
        }
    }

    private boolean isCommentCharacter(char currentChar) {
        return currentChar == '#';
    }

    private void eatCommentText(char currentChar) {
        while (currentChar != '\n') {
            currentPointer++;
            currentChar = input.charAt(currentPointer);
        }
    }

    private boolean isComment(char currentChar, char prviousChar) {
        return prviousChar == '/' && currentChar == '/';
    }

    private boolean isOperatorCharacter(char currentChar) {
        return currentChar == '+' || currentChar == '-' || currentChar == '*'
                || currentChar == '/' || currentChar == '(' || currentChar == ')';
    }

    private boolean isAssignmentOperator(char currentChar, char previousChar) {
        return previousChar == '<' && currentChar == '-';
    }

    private boolean isRelationalOperator(char currentChar) {
        return currentChar == '=' || currentChar == '!' ||
                currentChar == '<' || currentChar == '>';
    }

    private boolean isSeparatorCharacter(char currentChar) {
        return currentChar == ';' || currentChar == ','
                || currentChar == '{' || currentChar == '}' || currentChar == '['
                || currentChar == ']';
    }

    private Token handleIdentifiersAndKeywords(StringBuilder token, char currentChar) {
        while (Character.isJavaIdentifierPart(currentChar)) {
            token.append(currentChar);
            currentPointer++;
            currentChar = input.charAt(currentPointer);
        }

        String value = token.toString();
        //$TODO$ move these identifiers into a set
        final KeywordType keywordType = KeywordType.isKeyword(value);
        if (keywordType != null) {
            Keyword k = new Keyword(value, keywordType);
            tokenSet.put(++tokenCounter, k);
            return k;
        } else {
            Identifier i = new Identifier(value);
            tokenSet.put(++tokenCounter, i);
            return i;
        }
    }

    private Token handleConstants(StringBuilder token, char currentChar) {
        while (Character.isDigit(currentChar)) {
            token.append(currentChar);
            currentPointer++;
            currentChar = input.charAt(currentPointer);
        }
        Constant c = new Constant(token.toString());
        tokenSet.put(++tokenCounter, c);
        return c;
    }

    private char eliminateEmptySpaces() {
        char currentChar = input.charAt(currentPointer);
        while (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r') {
            currentChar = input.charAt(++currentPointer);
        }
        return currentChar;
    }


    public boolean hasNext() {
        return input.length() > currentPointer;
    }

    public static void main(String[] args) throws IOException {
        Tokenizer t = new Tokenizer("test/test011.txt");
        Token token;
        while (t.hasNext()) {
            token = t.next();
            Printer.print(" Token type: " + token.tokenType() + " \t " + token.getToken());
        }
    }
}
