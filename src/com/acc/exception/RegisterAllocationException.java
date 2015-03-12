package com.acc.exception;

import com.acc.ui.CompileInputFile;

/**
 * Created by Rumpy on 11-03-2015.
 */
public class RegisterAllocationException extends RuntimeException {
    public RegisterAllocationException(String message) {
        super(message + " in file ["+ CompileInputFile.currentFileName+"]");
    }
}
