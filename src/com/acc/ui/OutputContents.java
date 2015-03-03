package com.acc.ui;

import com.acc.parser.Computation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/23/2015.
 */
public class OutputContents {
    private List<Computation> programs = new ArrayList<Computation>();
    private Computation mainProgram;

    public OutputContents() {
    }

    public void addProgram(Computation program) {
        programs.add(program);
        if(program.getProgramName().equals("main")) {
            mainProgram = program;
        }
    }

    public List<Computation> getPrograms() {
        return programs;
    }

    public Computation getMainProgram() {
        return mainProgram;
    }
}
