package com.acc.graph;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.structure.BasicBlock;
import com.acc.structure.SymbolTable;

import java.util.*;

/**
 * Created by prabhuk on 2/26/2015.
 */
public class DeleteInstructions extends Worker {

    private final Code code;
    private Map<Integer, Instruction> destinationSource = new HashMap<Integer, Instruction>();
    private static int instructionNumber = 0;
    public DeleteInstructions(Code code, SymbolTable table) {
        super(table);
        this.code = code;
    }

    @Override
    public void visit(BasicBlock node) {
        final List<Instruction> instructions = node.getInstructions();
        final Iterator<Instruction> iterator = instructions.iterator();
        while (iterator.hasNext()) {
            final Instruction instruction = iterator.next();
            if(instruction.isDeleted()) {
                iterator.remove();
                code.removeCode(instruction);
            } else {
                updateDestinationsForOperand(instruction, instruction.getX());
                updateDestinationsForOperand(instruction, instruction.getY());
            }
        }
    }

    private void updateDestinationsForOperand(Instruction instruction, Result result) {
        if(result == null) {
            return;
        }
        if(result.kind().isVariable()) {
            destinationSource.put(result.getLocation(), instruction);
        } else if(result.kind().isIntermediate()) {
            destinationSource.put(result.getIntermediateLoation(), instruction);
        }
    }

    @Override
    public void finish() {
        final List<Instruction> instructions = code.getInstructions();
        final Collection<Integer> targets = destinationSource.keySet();
        for (Instruction instruction : instructions) {
            final Integer oldLocation = instruction.getLocation();
            if(targets.contains(oldLocation)) {
                final Instruction instruction1 = destinationSource.get(oldLocation);

                    handlePhiInstructionOperand(oldLocation, instruction1.getX());
                    handlePhiInstructionOperand(oldLocation, instruction1.getY());
            }
            instruction.setLocation(instructionNumber);
            instructionNumber++;
            targets.contains(oldLocation);
        }

    }

    private void handlePhiInstructionOperand(Integer oldLocation, Result result) {
        if(result == null) {
            return;
        }
        if(result.kind().isVariable()) {
            if (oldLocation.equals(result.getLocation())) {
                result.setLocation(instructionNumber);
            }
        } else if(result.kind().isIntermediate()) {
            if (oldLocation.equals(result.getIntermediateLoation())) {
                result.setIntermediateLoation(instructionNumber);
            }
        }
    }
}
