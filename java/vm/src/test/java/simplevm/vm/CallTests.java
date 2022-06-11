package simplevm.vm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static simplevm.vm.Bytecode.*;

public class CallTests {

    @Test void testCall() {
        VirtualMachine vm = new VirtualMachine();
        vm.execute(new int[] {
            // 0: entrypoint
            /*0*/ NOP,        // placeholder
            /*1*/ JMP, 25,
            // function countdown(count)
            //    expects count on top of stack; no return value
            /*3*/ STORE, 0,   // move count into locals[0]
            /*5*/ LOAD, 0,    // print locals[0]
            /*7*/ PRINT,
            /*8*/ LOAD, 0,    // if locals[0] == 0
            /*10*/EQ, 0,      
            /*12*/JT, 23,     // jump to return
            /*14*/LOAD, 0,    // locals[0] = locals[0] - 1
            /*16*/CONST, 1,
            /*18*/SUB,
            /*19*/STORE, 0,
            /*21*/JMP, 5,     // jump to top of loop
            /*23*/RET,
            /*24*/NOP,
            /*25*/CONST, 5,
            /*27*/CALL, 3     // CALL line 3
        });

        // Stack should be empty (there was no return from countdown())
        assertEquals(0, vm.getStack().length);
    }
    
}
