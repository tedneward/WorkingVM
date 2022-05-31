package simplevm.vm;

import java.util.Arrays;

import static simplevm.vm.Bytecode.*;

public class VirtualMachine {
    
    boolean trace = false;
    private void trace(String message) {
        if (trace) {
            System.out.println("TRACE: " + message);
        }
    }

    int sp = -1;
    int[] stack = new int[100];
    int[] getStack() {
        if (sp > -1)
            return Arrays.copyOf(stack, sp+1);
        else
            return new int[] { };
    }

    public void push(int value) {
        stack[++sp] = value;
        trace("pushed " + value + "; stack: " + Arrays.toString(stack));
    }
    public int pop() {
        return stack[sp--];
    }

    int ip = 0;
    public void execute(int opcode, int... operands) {
        switch (opcode) {
            case NOP:
                // Do nothing!
                trace("NOP");
                break;
            case DUMP:
                trace("DUMP");
                break;
            case TRACE:
                trace = !trace;
                trace("TRACE");
                break;
            
            case CONST:
                trace("CONST " + operands[0]);
                push(operands[0]);
                break;
            case POP:
                trace("POP");
                pop();
                break;

            default:
                throw new RuntimeException("Unrecognized opcode: " + opcode);
        }
    }
    public void execute(int[] code) {
        for (ip = 0; ip < code.length; )
        {
            switch (code[ip])
            {
                // 0-operand opcodes
                case NOP:
                case TRACE:
                case DUMP:
                case POP:
                    execute(code[ip]);
                    break;
                
                // 1-operand opcodes
                case CONST:
                    execute(code[ip], code[++ip]);
                    break;

                // 2-operand opcodes

                // Unknown
                default:
                    throw new RuntimeException("Unrecognized opcode: " + code[ip]);
            }
            ip++;
        }
    }
}
