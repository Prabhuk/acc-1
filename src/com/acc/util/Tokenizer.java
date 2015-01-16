package com.acc.util;

import com.acc.data.Operator;
import com.acc.data.Token;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Rumpy on 15-01-2015.
 *
 * The assignment operator <- has been converted to ` in getTokens method
 */
public class Tokenizer {
    private File sourceFile;
    private String input;
    private Set<Token> tokenSet;
    private StringTokenizer st;

    Tokenizer(String filePath)
    {
        sourceFile=new File(filePath);
        try
        {
            FileReader reader = new FileReader(sourceFile);
            char[] chars = new char[(int) sourceFile.length()];
            reader.read(chars);
            input = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tokenSet=new HashSet<Token>();
        input=input.replaceAll("\n"," ");
        input=input.replaceAll("\t"," ");
        input=input.replaceAll("<-","`");
        st = new StringTokenizer(input,", <>={}()`;",true);

    }


    Token getNext()
    {
        String e = st.nextToken();
        Operator o=new Operator(e);
        return o;

    }

//    boolean hasNext()
//    {
//
//    }

    public static void main(String[] args) {
        Tokenizer t=new Tokenizer("p1.txt");
    }
}
