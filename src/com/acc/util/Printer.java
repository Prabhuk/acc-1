package com.acc.util;

import com.acc.data.Instruction;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class Printer {
    /*
     * The print implementation could change to Logger or other smart implementations.
     */
    public static void print(String message) {
        System.out.println(message);
    }

    public static void debugMessage(String message) {

//        System.out.println(message);
    }

    public static void debugMessage(Instruction instruction) {
//        System.out.println(instruction.getLocation() + " " +instruction.getInstructionString());
    }
}
