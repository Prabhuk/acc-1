package com.acc.exception;

import com.acc.ui.CompileInputFile;

/**
 * Created by Rumpy on 03-03-2015.
 */
public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException(String message) {
        super(message + " in file ["+ CompileInputFile.currentFileName+"]");
    }
}
