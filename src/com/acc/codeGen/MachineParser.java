package com.acc.codeGen;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.exception.CodeGenerationException;
import com.acc.parser.Computation;
import com.acc.structure.Symbol;
import com.acc.structure.SymbolTable;
import com.acc.ui.OutputContents;
import com.acc.util.Printer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rumpy on 03-03-2015.
 */
public class MachineParser {
    private final OutputContents outputContents;
    Map<String, Integer> functionMap;
    ProcedureFixupTable procedureFixupTable;
    MachineCode machineCode;

    public MachineParser(OutputContents outputContents) {
        this.outputContents = outputContents;
        machineCode = new MachineCode();
        procedureFixupTable = new ProcedureFixupTable();
        functionMap = new HashMap<String, Integer>();
        functionMap.put("main", -1);
        AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.ADD, 29, 0, 30);
       // AuxilaryDLXFunctions.putF1(machineCode ,MachineOperationCode.ADD, 29,0,30);
    }

    public int[] begin() {
        parse(outputContents.getProgram("main"));
        Printer.debugMessage("\n\n\nPROGRAM STRT:");
        for (MachineInstruction i : machineCode.getInstructions()) {
            Printer.debugMessage(Integer.toString(i.getLocation()) + " : " + MachineOperationCode.getOperationName(i.opcode) + " " + Integer.toString(i.a) + " " + Integer.toString(i.b) + " " + Integer.toString(i.c));
        }
        return machineCode.getInstructionList();
    }


    public void parse(Computation program) {
        SymbolTable symboltable = program.getSymbolTable();
        Code programCode = program.getCode();
        List<Instruction> instructionSet = programCode.getInstructions();
        functionMap.put(program.getProgramName(), machineCode.getPc());   //Placing this function in the table of visited functions
        int parameterSize = 0;


        if (!program.getProgramName().equals("main")) {
            parameterSize = program.getFormalParams().size();
            procedureFixupTable.fix(program.getProgramName(), machineCode.getPc());  // Fixing all references to this function
            //PROLOGUE CONT.....
            //pushing the return address
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 31, 29, -4);
            //pushing old FP
            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 28, 29, -4);
            //making FP reg = SP reg
            AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.ADD, 28, 0, 29);
        }
        //Copying local symbol table into memory
        List<Symbol> localSymbols = symboltable.getLocalDeclarations();
//        for (Symbol s : localSymbols) {
//            Printer.debugMessage(s.getName());
//            AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 0, 29, -4);
//        }

        Map<Integer, Integer> instructionMap = new HashMap<Integer, Integer>(); // the key for this is the SSA instruction and the value is the machine instruction
        BranchFixupTable branchFixupTable = new BranchFixupTable();
        MemoryManager memoryManager = new MemoryManager(program, machineCode);


        for (Instruction currentInstruction : instructionSet) {
            instantiateVairables(localSymbols, currentInstruction);
            //System.out.println(currentInstruction.getLocation());
            branchFixupTable.fix(currentInstruction.getLocation(), machineCode.getPc());
            instructionMap.put(currentInstruction.getLocation(), machineCode.getPc());
            Printer.debugMessage("MACHINE PARSER: " + currentInstruction.getInstructionString());
            Integer instructionOpCode = currentInstruction.getOpcode();
            if (instructionOpCode == OperationCode.call) {
                 procedureCallTranslator(currentInstruction,memoryManager);
            } else if (instructionOpCode >= OperationCode.add && instructionOpCode <= OperationCode.cmp) {
                ArithmeticTranslator.translate(machineCode, currentInstruction, memoryManager);
                //Todo Have to deal with arrays!!
            } else if (instructionOpCode >= OperationCode.read && instructionOpCode <= OperationCode.writenl) {
                ReadWriteTranslator.translate(currentInstruction, machineCode, memoryManager);
            } else if (instructionOpCode >= OperationCode.bra && instructionOpCode <= OperationCode.bgt) {
                BranchTranslator.translate(currentInstruction, instructionMap, branchFixupTable, memoryManager, machineCode);
            } else if (instructionOpCode == OperationCode.move) {
                MoveTranslator.translate(currentInstruction, machineCode, memoryManager);
            }
            else if(instructionOpCode == OperationCode.ret)
            {
                if(currentInstruction.getX().isRegister())
                {
                    Result c = memoryManager.getOperand(currentInstruction,currentInstruction.getX(),false);
                    AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.ADD, 25, 0, c.regNo());
                }
                else if(currentInstruction.getX().isConstant()) {
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.ADDI, 25, 0, currentInstruction.getX().value());
                }
            }
            else if(instructionOpCode == OperationCode.end)
            {
                EndTranslator.translate(machineCode, program, parameterSize);
            }

        }

        for (String functionName : functionMap.keySet()) {
            if (functionMap.get(functionName).equals(-1)) {
                parse(outputContents.getProgram(functionName));
            }
        }

    }



    private int procedureCallTranslator(Instruction currentInstruction,MemoryManager memoryManager) {
        Integer dlxOpCode = MachineOperationCode.BSR;
        //PROLOGUE
        //pushing registers first
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 1, 29, -4);
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 2, 29, -4);
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 3, 29, -4);
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 4, 29, -4);
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 5, 29, -4);
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 6, 29, -4);
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 7, 29, -4);
        AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 8, 29, -4);
        //adding parameters as needed
        List<Result> parameters = currentInstruction.getParameters();
        if(parameters != null && !parameters.isEmpty()) {
            //Todo: What do i do with parameter values?
            for(Result parameter: parameters)
            {
               Result b = memoryManager.getOperand(currentInstruction, parameter, true);
                if(b.isConstant())
                {
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.ADDI, 27, 0, b.value());
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, 27, 29, -4);

                }
                else
                    AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.PSH, b.regNo(), 29, -4);
                //no pop yet
            }
        }
        if (!functionMap.containsKey(currentInstruction.getX().getVariableName())) {
            MachineInstruction machineInstruction = AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, 0, 0, -1);
            functionMap.put(currentInstruction.getX().getVariableName(), -1);
            procedureFixupTable.add(currentInstruction.getX().getVariableName(), machineInstruction);
        } else {
            Integer functionAddress = functionMap.get(currentInstruction.getX().getVariableName());
            if(functionAddress > machineCode.getPc())
                AuxilaryDLXFunctions.putF1(machineCode, dlxOpCode, 0, 0, functionAddress - machineCode.getPc());
        }
        Result a = memoryManager.getInstructionRegister(currentInstruction.getLocation());
        if(a.isRegister())
            AuxilaryDLXFunctions.putF2(machineCode, MachineOperationCode.ADD, a.regNo(), 0,25 );
        //I need to add this instruction to a map which contains the procedure name and all the BSRs to it.
        //This will later be used for fix-up purposes
        return parameters.size();
    }





























    //Possibly junk code
    private void instantiateVairables(List<Symbol> localSymbols, Instruction currentInstruction) {
        try {
            if (currentInstruction.getX().isVariable()) {
                int index = getSymbolIndex(currentInstruction.getX(), localSymbols);
                if (index == -1)
                    throw new CodeGenerationException("Undefined variable error");
                AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 26, 29, -(index * 4));
                currentInstruction.setX(new Result(Kind.REG, 26, null, null, null, null));
            }
            if (currentInstruction.getY().isVariable()) {
                int index = getSymbolIndex(currentInstruction.getY(), localSymbols);
                if (index == -1)
                    throw new CodeGenerationException("Undefined variable error");
                AuxilaryDLXFunctions.putF1(machineCode, MachineOperationCode.LDW, 26, 29, -(index * 4));
                currentInstruction.setY(new Result(Kind.REG, 27, null, null, null, null));
            }
        } catch (NullPointerException e) {
        }
    }

    private int getSymbolIndex(Result z, List<Symbol> localSymbols) {
        int index = 0;
        for (Symbol s : localSymbols) {
            if (z.getVariableName().equals(s.getName())) {
                return index;
            }
            index++;
        }
        return -1;
    }

}