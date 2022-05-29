package simplevm.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackTests {
    @Test void testPush() {
        VirtualMachine vm = new VirtualMachine();

        vm.push(27);

        assertEquals(1, vm.getStack().length);
        assertEquals(27, vm.getStack()[0]);
    }
    @Test void testTrace() {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, new Bytecode(43),
            Bytecode.NOP,
            Bytecode.DUMP
        };
        vm.execute(code);

        // If we got here, with no exception, we're good
        assertTrue(true);
    }
}
