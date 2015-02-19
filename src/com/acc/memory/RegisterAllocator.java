package com.acc.memory;

import com.acc.structure.Register;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhuk on 2/18/2015.
 */
public class RegisterAllocator {
    private static final List<Register> registers = new ArrayList<Register>(32);
//    private static final List<Integer> memory = new ArrayList<Integer>(1000);
    //$TODO$ need to load variables here

    static {
        //This should be read from a properties file
        int NUMBER_OF_REGISTERS = 32;
//        int MEMORY = 1000;
        //Create registers
        for(int i =0; i< NUMBER_OF_REGISTERS; i++) {
            registers.add(new Register());
        }

    }

    public static void deallocate(int regno) {
        registers.get(regno).setAvailable(true);
        registers.get(regno).setValue(null);
        //$TODO$ needs implementation
    }

    public static int allocateReg() {
        for (Register register : registers) {
            if(register.isAvailable()) {
                register.setAvailable(false);
                return registers.indexOf(register);
            }
        }
        throw new RuntimeException("Registers are full");
    }

}
