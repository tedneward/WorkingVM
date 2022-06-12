package simplevm.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static simplevm.vm.Bytecode.*;

public class CallTests {

    @Test void testCall() {
        VirtualMachine vm = new VirtualMachine();
        vm.execute(new int[] {
            // 0: entrypoint
            /*0*/ NOP,//TRACE,
            /*1*/ JMP, 25,
            // function countdown(count)
            //    expects count on top of stack; no return value
            /*3*/ STORE, 0,   // move count into locals[0]
            /*5*/ LOAD, 0,    // locals[0]
            /*7*/ PRINT,      // print
            /*8*/ LOAD, 0,    // locals[0]
            /*10*/CONST, 0,   // 0
            /*12*/EQ,         // locals[0] == 0 ?
            /*13*/JT, 24,     // jump to return
            /*15*/LOAD, 0,    // locals[0] = locals[0] - 1
            /*17*/CONST, 1,
            /*19*/SUB,
            /*20*/STORE, 0,
            /*22*/JMP, 5,     // jump to top of loop
            /*24*/RET,
            // end
            /*25*/CONST, 5,
            /*27*/CALL, 3,    // CALL line 3
            /*29*/NOP
        });

        // Stack should be empty (there was no return from countdown())
        assertEquals(0, vm.getStack().length);
    }
    
}
