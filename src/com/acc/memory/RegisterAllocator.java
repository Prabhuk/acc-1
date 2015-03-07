package com.acc.memory;

import com.acc.data.Instruction;
import com.acc.parser.Computation;
import com.acc.ra.structure.InterferenceGraph;
import com.acc.structure.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by prabhuk on 2/18/2015.
 */
public class RegisterAllocator {

    private final Computation contents;
    private final Set<Instruction> phis;
    private final InterferenceGraph graph;

    public RegisterAllocator(Computation program, Set<Instruction> phis, InterferenceGraph graph) {
        this.contents = program;
        this.phis = phis;
        this.graph = graph;
    }



}
