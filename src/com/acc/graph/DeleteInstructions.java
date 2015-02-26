package com.acc.graph;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.parser.Parser;
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
                final Integer opcode = instruction.getOpcode();
                if (opcode >= OperationCode.bne && opcode <= OperationCode.bgt) {
                    destinationSource.put(instruction.getY().value(), instruction);
                } else if (opcode == OperationCode.bra) {
                    destinationSource.put(instruction.getX().value(), instruction);
                }
            }
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
                if(instruction1.getOpcode() == OperationCode.bra) {
                    instruction1.getX().value(instructionNumber);
                } else {
                    instruction1.getY().value(instructionNumber);
                }
            }
            instruction.setLocation(instructionNumber);
            instructionNumber++;
            targets.contains(oldLocation);
        }

    }
}
