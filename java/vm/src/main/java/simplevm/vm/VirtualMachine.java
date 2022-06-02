package simplevm.vm;

import java.util.Arrays;

import static simplevm.vm.Bytecode.*;

public class VirtualMachine {

    public static class Exception extends java.lang.RuntimeException {
        public Exception(String message) {
            super(message);
        }
    }
    
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

    int[] globals = new int[256];

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

            // Binary math operations
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
            {
                // We are assuming left-to-right parameter order
                int rhs = pop();
                int lhs = pop();
                switch (opcode) {
                    case ADD: trace("ADD"); push(lhs + rhs); break;
                    case SUB: trace("SUB"); push(lhs - rhs); break;
                    case MUL: trace("MUL"); push(lhs * rhs); break;
                    case DIV: trace("DIV"); push(lhs / rhs); break;
                    case MOD: trace("MOD"); push(lhs % rhs); break;
                    default: throw new Exception("Should never happen");
                }
                break;
            }
            // Unary math operations
            case ABS:
                trace("ABS");
                push(Math.abs(pop()));
                break;
            case NEG:
                trace("NEG");
                push(- pop());
                break;

            // Comparison ops
            case EQ:
            case NEQ:
            case GT:
            case LT:
            case GTE:
            case LTE:
            {
                // We are assuming left-to-right parameter order
                int rhs = pop();
                int lhs = pop();
                switch (opcode) {
                    case EQ: trace("EQ"); push(lhs == rhs ? 1 : 0); break;
                    case NEQ: trace("NEQ"); push(lhs != rhs ? 1 : 0); break;
                    case GT: trace("GT"); push(lhs > rhs ? 1 : 0); break;
                    case LT: trace("LT"); push(lhs < rhs ? 1 : 0); break;
                    case GTE: trace("GTE"); push(lhs >= rhs ? 1 : 0); break;
                    case LTE: trace("LTE"); push(lhs <= rhs ? 1 : 0); break;
                    default:
                        throw new Exception("Should never reach here");
                }
                break;
            }

            // Branching ops
            case JMP:
            {
                trace("JMP" + operands[0]);
                ip = operands[0];
                break;
            }
            case RJMP:
            {
                trace("RJMP" + operands[0]);
                ip += operands[0];
                break;
            }
            case JMPI:
            {
                trace("JMPI");
                int location = pop();
                ip = location;
                break;
            }
            case RJMPI:
            {
                trace("RJMPI");
                int offset = pop();
                ip += offset;
                break;
            }
            case JZ:
            {
                trace("JZ" + operands[0]);
                int jump = pop();
                if (jump == 0) { 
                    ip = operands[0];
                }
                break;
            }
            case JNZ:
            {
                trace("JNZ" + operands[0]);
                int jump = pop();
                if (jump != 0) { 
                    ip = operands[0];
                }
                break;
            }

            default:
                throw new Exception("Unrecognized opcode: " + opcode);
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
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                case MOD:
                case ABS:
                case NEG:
                case EQ:
                case NEQ:
                case GT:
                case LT:
                case GTE:
                case LTE:
                case JMPI:
                case RJMPI:
                    execute(code[ip]);
                    break;
                
                // 1-operand opcodes
                case CONST:
                case JMP:
                case RJMP:
                case JZ:
                case JNZ:
                    execute(code[ip], code[++ip]);
                    break;

                // 2-operand (or more) opcodes

                // Unknown
                default:
                    throw new Exception("Unrecognized opcode: " + code[ip]);
            }
            ip++;
        }
    }
}
