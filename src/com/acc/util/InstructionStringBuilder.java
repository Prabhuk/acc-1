package com.acc.util;

import com.acc.constants.OperationCode;
import com.acc.constants.SSAOpCodes;
import com.acc.data.Instruction;
import com.acc.data.PhiInstruction;
import com.acc.data.Result;
import com.acc.structure.Symbol;

/**
 * Created by prabhuk on 2/20/2015.
 */
public class InstructionStringBuilder {

    public static String getSSA(int opcode, Integer a, Integer b, Integer c, Symbol symbol,
                                Symbol lhs, Symbol rhs, Instruction instruction) {

        final String operationName = SSAOpCodes.opcodeAndNames.get(opcode);
        if(operationName == null) {
            return "";
//            return getDLXInstruction(opcode, a, b, c, symbol, lhs, rhs);
        }
        if(opcode == SSAOpCodes.MOV) {
            return buildSSAMoveInstruction(lhs, rhs);
        }
        if(opcode ==SSAOpCodes.PHI) {
            return buildSSAPhiInstruction(symbol, instruction);
        }
        StringBuilder sb;
        sb = new StringBuilder(operationName).append(" ");

        addSSAa(opcode, symbol, sb);
        addSSAb(opcode, symbol, sb);

        return sb.toString();
    }

    private static void addSSAb(int opcode, Symbol symbol, StringBuilder sb) {
        if(!SSAOpCodes.excludeB.contains(opcode)) {
            final Result result = symbol.getResult();
            if(result.kind().isVariable() || result.kind().isArray()) {
                sb.append(result.address());
            } else if(result.kind().isConstant()) {
                sb.append(result.value());
            } else if(result.kind().isFixupDummy()){
                sb.append(result.fixupLoc());
            } else {
                sb.append(result.regNo());
            }
        }
        sb.append(" ");
    }



    private static void addSSAa(int opcode, Symbol symbol, StringBuilder sb) {
        if(!SSAOpCodes.excludeA.contains(opcode)) {
                sb.append(symbol.getName()).append(" ");
        }
    }

    private static String buildSSAPhiInstruction(Symbol symbol, Instruction instruction) {
        StringBuilder sb = new StringBuilder(SSAOpCodes.opcodeAndNames.get(SSAOpCodes.PHI));
        final PhiInstruction phi = (PhiInstruction) instruction;
        if(phi.isComplete()) {
            Symbol lhs = phi.getLeftSymbol();
            Symbol rhs = phi.getRightSymbol();
            sb.append(" ").append(symbol.getName()).append(" ").append(lhs.getUniqueIdentifier()).append(" ").append(rhs.getUniqueIdentifier());
        }
        return sb.toString();
    }


    private static String buildSSAMoveInstruction(Symbol lhs, Symbol rhs) {
        StringBuilder sb = new StringBuilder(SSAOpCodes.opcodeAndNames.get(SSAOpCodes.MOV)).append(" ");
        if (rhs.getResult().kind().isRegister()) {
            sb.append("(R").append(rhs.getResult().regNo()).append(")");
        } else if (rhs.getResult().kind().isVariable()) { //variable
            sb.append(String.valueOf(rhs.getName()));
        } else {
            sb.append(String.valueOf(rhs.getResult().value()));
        }

        if (lhs.getType().isArray()) {
            //$TODO$ Generate ADDA
            sb.append(",").append(lhs.getName());
            for (Result result : lhs.getArrayIdentifiers()) {
                //$TODO$ result types must be handled
                sb.append("[");
                if (result.kind().isConstant()) {
                    sb.append(result.value());
                } else if (result.kind().isVariable()) {
                    sb.append("(");
                    sb.append(result.address());
                    sb.append(")");
                } else {
                    sb.append("(R");
                    sb.append(result.regNo());
                    sb.append(")");
                }
                sb.append("]");
            }
        } else {
            sb.append(",").append(lhs.getName());
        }
        return sb.toString();
    }


    public static String getDLXInstruction(int opcode, Integer a, Integer b, Integer c, Symbol symbol,
                                           Symbol lhs, Symbol rhs) {


        final String operationName = OperationCode.opcodeAndNames.get(opcode);
        final StringBuilder sb = new StringBuilder(operationName).append(" ");
        if (opcode == OperationCode.MOV) {
            buildMoveInstruction(sb, lhs, rhs);
        } else {
            boolean addComma = false;
            if (a != null && !OperationCode.excludeA.contains(opcode)) {
                if (opcode == 15) {
                    sb.append(symbol.getUniqueIdentifier());
                } else {
                    sb.append(String.valueOf(a));
                }
                addComma = true;
            }
            if (b != null && !OperationCode.excludeB.contains(opcode)) {
                if (addComma) {
                    sb.append(",");
                }
                addComma = true;
                sb.append(String.valueOf(b));
            }
            if (c != null) {
                if (addComma) {
                    sb.append(",");
                }
                sb.append(String.valueOf(c));
            }
        }
        return sb.toString();


    }


    private static void buildMoveInstruction(StringBuilder sb, Symbol lhs, Symbol rhs) {
        sb.append(SSAOpCodes.opcodeAndNames.get(SSAOpCodes.MOV)).append(" ");
        if (rhs.getResult().kind().isRegister()) {
            sb.append("(R").append(rhs.getResult().regNo()).append(")");
        } else if (rhs.getResult().kind().isVariable()) { //variable
            //Should be address field
            sb.append("(").append(String.valueOf(rhs.getResult().address())).append(")");
        } else {
            sb.append(String.valueOf(rhs.getResult().value()));
        }

        if (lhs.getType().isArray()) {
            sb.append(",").append(lhs.getUniqueIdentifier());
            for (Result result : lhs.getArrayIdentifiers()) {
                //$TODO$ result types must be handled
                sb.append("[");
                if (result.kind().isConstant()) {
                    sb.append(result.value());
                } else if (result.kind().isVariable()) {
                    sb.append("(");
                    sb.append(result.address());
                    sb.append(")");
                } else {
                    sb.append("(R");
                    sb.append(result.regNo());
                    sb.append(")");
                }
                sb.append("]");
            }
        } else {
            sb.append(",").append(lhs.getUniqueIdentifier());
        }
    }


}
