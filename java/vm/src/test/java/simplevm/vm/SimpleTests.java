package simplevm.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static simplevm.vm.Bytecode.*;

class SimpleTests {
    @Test void testTest() {
        assertEquals(2, 1 + 1);
        //assertEquals(2, 1 - 1); // just to fail-test, just in case
    }
    @Test void testInstantiation() {
        VirtualMachine vm = new VirtualMachine();

        assertTrue(vm != null);
    }
    @Test void testNOP() {
        VirtualMachine vm = new VirtualMachine();

        vm.execute(NOP);

        // If we got here, with no exception, we're good
        assertTrue(true);
    }
    @Test void testNOPs() {
        VirtualMachine vm = new VirtualMachine();

        vm.execute(new int[] {
            NOP,
            NOP,
            NOP,
            NOP
        });

        // If we got here, with no exception, we're good
        assertTrue(true);
    }
    @Test void testDump() {
        VirtualMachine vm = new VirtualMachine();

        vm.execute(DUMP);

        // If we got here, with no exception, we're good
        assertTrue(true);
    }
    @Test void testTrace() {
        VirtualMachine vm = new VirtualMachine();

        vm.execute(new int[] {
            TRACE,
            NOP,
            DUMP
        });

        // If we got here, with no exception, we're good
        assertEquals(0, vm.getStack().length);
    }
    @Test void testPrint() {
        VirtualMachine vm = new VirtualMachine();

        vm.execute(new int[] {
            CONST, 12,
            PRINT
        });

        // Check the output for the printed output

        assertEquals(0, vm.getStack().length);
    }
}
