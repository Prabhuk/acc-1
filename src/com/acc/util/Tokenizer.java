package com.acc.util;

import com.acc.data.Operator;
import com.acc.data.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Rumpy on 15-01-2015.
 * <p/>
 * The assignment operator <- has been converted to ` in getTokens method
 */
public class Tokenizer {
    private File sourceFile;
    private String input;
    private Set<Token> tokenSet;
    private StringTokenizer st;

    public Tokenizer(String filePath) throws IOException {
        sourceFile = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(sourceFile));
            char[] chars = new char[(int) sourceFile.length()];
            reader.read(chars);
            input = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
        tokenSet=new HashSet<Token>();
        input=input.replaceAll("\n"," ");
        input=input.replaceAll("\t"," ");
        input=input.replaceAll("<-","`");
        st = new StringTokenizer(input,", <>={}()`;",true);
    }


    public Token next() {
        String e = st.nextToken();
        //$TODO$ figure out what kind of token it is before initializing it as Operator
        Operator o=new Operator(e);
        return o;
    }

    public boolean hasNext()
    {
        return true;
    }

    public static void main(String[] args) throws IOException {
        Tokenizer t = new Tokenizer("p1.txt");
    }
}
