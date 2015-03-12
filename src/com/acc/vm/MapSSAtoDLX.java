package com.acc.vm;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.exception.UnknownOperatorException;
import com.acc.vm.DLX;
import com.acc.vm.DLXInstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 3/10/2015.
 */
public class MapSSAtoDLX {

    public MapSSAtoDLX(Code code, Map<Integer, Integer> regInfo) {

        final RegisterMapper reg = new RegisterMapper(regInfo);

        List<Integer> instructions = new ArrayList<Integer>();
        for (Instruction instruction : code.getInstructions()) {
            final Integer operandCount = OperationCode.getOperandCount(instruction.getOpcode());
            Integer a = null;
            Integer b = null;
            Integer c = null;

            if(operandCount > 0) {
                final Result x = instruction.getX();
                a = handleOperand(reg, x);
            }

            if(operandCount > 1) {
                b = handleOperand(reg, instruction.getY());
            }

            if(operandCount > 2) {
                c = reg.getRegisterNumber(instruction.getLocation());
            }
        }




        List<DLXInstruction> dlx = new ArrayList<DLXInstruction>();
        for (Instruction instruction : code.getInstructions()) {
            final Integer operandCount = OperationCode.getOperandCount(instruction.getOpcode());
            Result a = null;
            Result b = null;
            Result c = null;
            if(operandCount > 0) {
                final Result x = instruction.getX();
                if(x.isIntermediate()) {
                    final Integer regNo = regInfo.get(x.getIntermediateLoation());
                    a = new Result(Kind.REG);
                    a.regNo(regNo);
                } else {
                    a = x;
                }
            }

            if(operandCount > 1) {
                final Result y = instruction.getY();
                if(y.isIntermediate()) {
                    final Integer regNo = regInfo.get(y.getIntermediateLoation());
                    b = new Result(Kind.REG);
                    b.regNo(regNo);
                } else {
                    b = y;
                }

            }

            final Integer target = regInfo.get(instruction.getLocation());
            if(target != null) {
                c = new Result(Kind.REG);
                c.regNo(target);
            }

//            final DLXInstruction dlxCode = new DLXInstruction(instruction.getOpcode(), a, b, c);
//            System.out.println(dlxCode);
        }
    }

    protected Integer handleOperand(RegisterMapper reg, Result x) {
        Integer a = null;
        if(x.isIntermediate()) {
            a = reg.getRegisterNumber(x.getIntermediateLoation());
        } else if (x.isConstant()) {
            a = x.value();
        } else if (x.isVariable()) {
            //$TODO$ handle variable
        } else {
            throw new UnknownOperatorException("Unknown result type");
        }
        return a;
    }
}
