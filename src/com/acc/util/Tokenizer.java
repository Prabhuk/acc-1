package com.acc.util;

import com.acc.data.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.lang.Character;

/**
 * Created by Rumpy on 15-01-2015.
 * <p/>
 * The assignment operator <- has been converted to ` in getTokens method
 * The "(" and ")" bracket are always defined as operators in this class
 */
public class Tokenizer {
    private File sourceFile;
    private String input;
    private Set<Token> tokenSet;
    private StringTokenizer st;
    private int currentPointer = 0;

    public Tokenizer(String filePath) throws IOException {
        sourceFile = new File(filePath);
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
        tokenSet = new HashSet<Token>();
        //input = input.replaceAll("\n", " ");
        //input = input.replaceAll("\t", " ");
        //input = input.replaceAll("<-", "`");
        //input = input.replaceAll("//", "~");
        //st = new StringTokenizer(input, ", <>={}()`;", true);
    }


    public Token next() {
        if (!hasNext()) {
            throw new UnsupportedOperationException("You should call next only when hasNext is true");
        }

        StringBuilder token = new StringBuilder();
        char currentChar = eliminateEmptySpaces();
        if(currentChar == '.') {
            Printer.print("DEBUG: currentPointer ["+ currentPointer + "] inputLength ["+input.length()+"]" );
            currentPointer++;
            return new Separator(token.append(currentChar).toString());
        }

        if (Character.isDigit(currentChar)) {
            return handleConstants(token, currentChar);
        } else if (Character.isAlphabetic(currentChar)) {
            return handleIdentifiersAndKeywords(token, currentChar);
        } else if (isSeparatorCharacter(currentChar)) {
            currentPointer++;
            return new Separator(token.append(currentChar).toString());
        } else if (isRelationalOperator(currentChar)) {
            char previous = currentChar;
            token.append(currentChar);
            currentPointer++;
            currentChar = input.charAt(currentPointer);
            if (isAssignmentOperator(currentChar, previous)) {
                token.append(currentChar);
                currentPointer++;
                return new AssignmentOperator(token.toString());
            } else if (currentChar == '=') {
                token.append(input.charAt(currentPointer++));
            }
            return new RelOp(token.toString());
        } else if (isOperatorCharacter(currentChar)) {
            char temp = currentChar;
            token.append(temp);
            currentPointer++;
            currentChar = input.charAt(currentPointer);
            if (isComment(currentChar, temp)) {
                while (currentChar != '\n') {
                    currentPointer++;
                    currentChar = input.charAt(currentPointer);
                }
                return next();
            }
            return new Operator(token.toString());
        } else {
            currentPointer++;
            throw new RuntimeException("Unrecognized character ["+ currentChar +"]");
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
        if ("if".equals(value) || "let".equals(value) || "fi".equals(value) || "call".equals(value)
                || "then".equals(value) || "else".equals(value) || "while".equals(value) ||
                "do".equals(value) || "od".equals(value) || "return".equals(value) || "var".equals(value) ||
                "array".equals(value) || "function".equals(value) || "procedure".equals(value) || "main".equals(value)) {
            return new Keyword(value);
        } else {
            return new Identifier(value);
        }
    }

    private Token handleConstants(StringBuilder token, char currentChar) {
        while (Character.isDigit(currentChar)) {
            token.append(currentChar);
            currentPointer++;
            currentChar = input.charAt(currentPointer);
        }
        return new Constant(token.toString());
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
        Tokenizer t = new Tokenizer("p1.txt");
        Token token;
        while (t.hasNext()) {
            token = t.next();
            Printer.print(" Token type: " + token.tokenType() + " \t " + token.getToken());
        }
    }
}
