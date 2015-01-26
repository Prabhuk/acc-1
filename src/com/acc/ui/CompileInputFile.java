package com.acc.ui;

import com.acc.data.Token;
import com.acc.util.Tokenizer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prabhuk on 1/14/2015.
 */
public class CompileInputFile {
    private String filePath;
    private Tokenizer tokenizer;
    private static Logger logger = Logger.getLogger(CompileInputFile.class.getName());

    public CompileInputFile(String filePath) {
        this.filePath = filePath;
        try {
            tokenizer = new Tokenizer(filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Input file [" + filePath + "] not found");
            e.printStackTrace();
        }
        processFile();
    }

    private void processFile() {
        final Token next = tokenizer.next();
        switch (next.getTokenType()) {
        }
    }
}
