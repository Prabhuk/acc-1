package com.acc.vm;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Instruction;
import com.acc.data.Result;
import com.acc.ui.OutputContents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabhuk on 3/10/2015.
 */
public class MapSSAtoDLX {


    private final Register reg;
    final List<Integer> instructions = new ArrayList<Integer>();
    private static int BEGIN_STACK_POINTER = 5000;
    private static int BEGIN_FRAME_POINTER = 3000;
    private final OutputContents contents;
    private Instruction tempInstruction = null;
    Map<Integer, Integer> ssaBranchTargetLocationMCIindex = new HashMap<Integer, Integer>();
    Map<Integer, Integer> cmpLocationsForBackwardBranch = new HashMap<Integer, Integer>();
    Map<Integer, Integer> BRAtargetLocationMCIindex = new HashMap<Integer, Integer>();

    public MapSSAtoDLX(OutputContents contents, Code code, Map<Integer, Integer> regInfo) {
        this.reg = new Register(regInfo);
        this.contents = contents;

        prefixCode();
        later(code, regInfo, reg);
        endCode();
    }

    private void prefixCode() {
        this.instructions.add(DLX.assemble(DLX.ADDI, Register.FP, 0, BEGIN_FRAME_POINTER)); //loading framepointer location
        this.instructions.add(DLX.assemble(DLX.ADDI, Register.SP, 0, BEGIN_STACK_POINTER)); //loading stackpointer location
//        this.instructions.add(DLX.assemble(DLX.RET, Register.SP)); // JUMPING TO BEGINNING OF STACK POINTER
    }

    private void endCode() {
//        this.instructions.add(DLX.assemble(DLX.ADDI, Register.DUMMY_REGISTER, 0, 0));
        this.instructions.add(DLX.assemble(DLX.RET, 0));
    }

    private void translate(Instruction instruction) {
        if(ssaBranchTargetLocationMCIindex.containsKey(instruction.getLocation())) {
            fixUpBranchTarget(instruction, ssaBranchTargetLocationMCIindex);
        }

        if(BRAtargetLocationMCIindex.containsKey(instruction.getLocation())) {
            fixUpBranchTarget(instruction, BRAtargetLocationMCIindex);
        }

        final Integer ssaCode = instruction.getOpcode();
        final Integer operandCount = OperationCode.getOperandCount(ssaCode);
//        List<Integer> machineCode = new ArrayList<Integer>();



        handleNoOperands(instruction, ssaCode, operandCount);
        handleOneOperand(instruction, ssaCode, operandCount);
        handleTwoOperands(instruction, ssaCode, operandCount);


    }

    private void fixUpBranchTarget(Instruction instruction, Map<Integer, Integer> map) {
        final Integer mciTarget = map.get(instruction.getLocation());
        final int branchOffset = instructions.size() - mciTarget;
        int mci = instructions.get(mciTarget);
        final int updatedInstruction = DLX.assemble(mci >>> 26, (mci >>> 21) & 0x1F, branchOffset);
        instructions.set(mciTarget, updatedInstruction);
    }

    private int getBackwardBranchTarget(Instruction instruction) {
        final Integer mciTarget = cmpLocationsForBackwardBranch.get(instruction.getLocation());
        return mciTarget - instructions.size();
    }

    private void handleTwoOperands(Instruction instruction, Integer ssaCode, Integer operandCount) {
        if(operandCount == 2) {
            if(ssaCode == OperationCode.cmp) {
                cmpLocationsForBackwardBranch.put(instruction.getLocation(), instructions.size());
            }
            if (ssaCode >= OperationCode.add && ssaCode <= OperationCode.cmp) {
                final Result x = instruction.getX();
                final Result y = instruction.getY();
                int xReg = loadOperand(x, Register.DUMMY_REGISTER);
                int yReg = loadOperand(y, Register.DUMMY_REGISTER_2);
                int target = reg.getRegisterNumber(instruction.getLocation());
                this.instructions.add(DLX.assemble(OpcodeMapper.arithmetic.get(ssaCode), target, xReg, yReg));
            }

            if(ssaCode == OperationCode.move) {
                this.instructions.add(DLX.assemble(DLX.ADDI, reg.getRegisterNumber(instruction.getX().regNo()), 0, loadOperand(instruction.getY(), Register.DUMMY_REGISTER)));
            }

            if(ssaCode == OperationCode.store) {
                //$TODO$ tempInstruction has adda instruction
            }

            if(ssaCode >= OperationCode.bne && ssaCode <= OperationCode.bgt) {
                final int xreg = loadOperand(instruction.getX(), Register.DUMMY_REGISTER);
                ssaBranchTargetLocationMCIindex.put(instruction.getY().value(), this.instructions.size());
                this.instructions.add(DLX.assemble(OpcodeMapper.branch.get(ssaCode), xreg, 0)); //Fixed up later
            }

            if(ssaCode == OperationCode.adda) {
                tempInstruction = instruction;
            }
        }
    }

    private int loadOperand(Result operand, int targetRegister) {
        int xReg;
        if(operand.isConstant()) {
            this.instructions.add(DLX.assemble(DLX.ADDI, targetRegister, 0, operand.value()));
            xReg = targetRegister;
        } else if(operand.isRegister()) {
            xReg = reg.getRegisterNumber(operand.regNo());
        } else if(operand.isIntermediate()) {
            xReg = reg.getRegisterNumber(operand.getIntermediateLoation());
        } else {
            throw new RuntimeException("Unhandled result type");
        }
        return xReg;
    }

    private void handleOneOperand(Instruction instruction, Integer ssaCode, Integer operandCount) {
        if(operandCount == 1) {
            if(ssaCode == OperationCode.write) {
                final Result targetLocaion = instruction.getX();
                instructions.add(DLX.assemble(DLX.WRD, getTargetRegister(targetLocaion)));
            } else if (ssaCode == OperationCode.load) {
                if(tempInstruction != null && tempInstruction.getOpcode() == OperationCode.adda) {
                    //$TODO$ tempHolds adda and load holds target
                }
            } else if (ssaCode == OperationCode.bra) {
                if(cmpLocationsForBackwardBranch.containsKey(instruction.getX().value())) {
                    instructions.add(DLX.assemble(DLX.BSR, getBackwardBranchTarget(instruction)));
                } else {
                    BRAtargetLocationMCIindex.put(instruction.getX().value(), instructions.size());
                    instructions.add(DLX.assemble(DLX.BSR, 0)); //$Fixed up later
                    //Forward branch in case of else block's presence

                }
                //$TODO$ handle branches
            } else if (ssaCode == OperationCode.call) {
                //$TODO$ load function if it is not loaded already
            }
        }
    }

    private int getTargetRegister(Result targetLocaion) {
        int regNo;
        if(targetLocaion.isConstant()) {
            instructions.add(DLX.assemble(DLX.ADDI, Register.DUMMY_REGISTER, 0, targetLocaion.value()));
            regNo = Register.DUMMY_REGISTER;
        } else if(targetLocaion.isIntermediate()) {
            regNo = reg.getRegisterNumber(targetLocaion.getLocation());
        } else if(targetLocaion.isRegister()) {
            regNo = reg.getRegisterNumber(targetLocaion.regNo());
        } else {
            throw new RuntimeException("Handle Variables");
        }
        return regNo;
    }

    private void handleNoOperands(Instruction instruction, Integer ssaCode, Integer operandCount) {
        if(operandCount == 0) {
            if(ssaCode == OperationCode.read) {
                final int regNo = reg.getRegisterNumber(instruction.getLocation());
                instructions.add(DLX.assemble(DLX.RDI, regNo));
            } else if(ssaCode == OperationCode.writenl) {
                instructions.add(DLX.assemble(DLX.WRL));
            }
        }
    }

    protected void later(Code code, Map<Integer, Integer> regInfo, Register reg) {
        for (Instruction instruction : code.getInstructions()) {
            translate(instruction);
        }
    }

    public int[] getInstructionList() {
        int DLXInstructions[] = new int[instructions.size()];
        int i=0;
        for (Integer instruction : instructions) {
            DLXInstructions[i++]=instruction;
        }
        return DLXInstructions;
    }

}
