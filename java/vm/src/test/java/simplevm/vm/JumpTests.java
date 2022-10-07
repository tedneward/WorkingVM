package simplevm.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static simplevm.vm.Bytecode.*;

public class JumpTests {
    @Test void testJMP() {
        VirtualMachine vm = new VirtualMachine();
        vm.execute(new int[] {
            JMP, 3,  // bypass FATAL opcode
            FATAL,
            NOP
        });

        assertEquals(0, vm.getStack().length);
    }
    @Test void testLotsofJumps() {
        VirtualMachine vm = new VirtualMachine();
        vm.execute(new int[] {
            /* 0*/ NOP,//TRACE,
            /* 1*/ JMP, 4,  // bypass FATAL opcode
            /* 3*/ FATAL,
            /* 4*/ JMP, 7,
            /* 6*/ FATAL,
            /* 7*/ JMP, 10,
            /* 9*/ FATAL,
            /*10*/ NOP
        });

        assertEquals(0, vm.getStack().length);
    }
    @Test void testCountdownImplementation() {
        System.out.println("testCountdownImplementation");
        VirtualMachine vm = new VirtualMachine();
        vm.execute(new int[] {
            // countdown from 13 to 10
            /* 0*/ TRACE,       // tracing on/off
            /* 1*/ CONST, 13,   // store 13 (starting count)...
            /* 3*/ GSTORE, 0,   // move count into globals[0]
            /* 5*/ GLOAD, 0,    // globals[0]
            /* 7*/ PRINT,       // print
            /* 8*/ GLOAD, 0,    // globals[0]
            /*10*/ CONST, 10,   // 10
            /*12*/ EQ,          // globals[0] == 10 ?
            /*13*/ JNZ, 24,     // jump to end
            /*15*/ GLOAD, 0,    // globals[0] = globals[0] - 1
            /*17*/ CONST, 1,
            /*19*/ SUB,
            /*20*/ GSTORE, 0,
            /*22*/ JMP, 5,      // jump to top of loop
            /*24*/ DUMP
        });

        //assertEquals(0, vm.getStack().length);
        assertEquals(10, vm.getGlobals()[0]);
    }
}
