package simplevm.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static simplevm.vm.Bytecode.*;

public class GlobalTests {
    @Test void testStore() {
        VirtualMachine vm = new VirtualMachine();

        vm.execute(new int[] {
            CONST, 42,
            GSTORE, 0
        });

        assertEquals(0, vm.getStack().length);
        assertEquals(42, vm.getGlobals()[0]);
    }
    @Test void testLoad() {
        VirtualMachine vm = new VirtualMachine();

        vm.getGlobals()[0] = 12;
        vm.execute(new int[] {
            GLOAD, 0
        });

        // If we got here, with no exception, we're good
        assertEquals(1, vm.getStack().length);
        assertEquals(12, vm.getGlobals()[0]);
    }
    @Test void testLoadAndStore() {
        VirtualMachine vm = new VirtualMachine();

        vm.execute(new int[] {
            CONST, 42,
            GSTORE, 0,
            CONST, 3,
            GSTORE, 1,
            GLOAD, 0,
            GLOAD, 1,
            ADD,
            GSTORE, 2
        });

        assertEquals(0, vm.getStack().length);
        assertEquals(42, vm.getGlobals()[0]);
        assertEquals(3, vm.getGlobals()[1]);
        assertEquals(45, vm.getGlobals()[2]);
    }
    @Test void testCountdownImplementation() {
        VirtualMachine vm = new VirtualMachine();
        vm.execute(new int[] {
            // countdown from 13 to 10
            /* 0*/ TRACE,       // tracing on/off
            /* 1*/ CONST, 13,   // store 13 (starting count)...
            /* 3*/ GSTORE, 0,   // ... stored into globals[0]
            /* 5*/ GLOAD, 0,    // load globals[0]/count
            /* 7*/ PRINT,       // print
            /* 8*/ GLOAD, 0,    // globals[0]/count
            /*10*/ CONST, 10,   // 10
            /*12*/ EQ,          // globals[0]/count == 0 ?
            /*13*/ JNZ, 24,      // jump to return
            /*15*/ GLOAD, 0,    // globals[0] = globals[0] - 1
            /*17*/ CONST, 1,    // 1
            /*19*/ SUB,         // subtract (rhs: 1, lhs: globals[0])
            /*20*/ GSTORE, 0,   // store result back to globals[0]/count
            /*22*/ JMP, 5,      // jump to top of loop
        });

        assertEquals(0, vm.getStack().length);
        assertEquals(10, vm.getGlobals()[0]);
    }
}
