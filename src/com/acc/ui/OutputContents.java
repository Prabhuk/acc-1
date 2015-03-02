package com.acc.ui;

import com.acc.parser.Computation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/23/2015.
 */
public class OutputContents {
    private static List<Computation> programs = new ArrayList<Computation>();

    public static void addProgram(Computation program) {
        programs.add(program);
    }

    public static List<Computation> getPrograms() {
        return programs;
    }

    public static void clear() {
        programs = new ArrayList<Computation>();
    }
}
