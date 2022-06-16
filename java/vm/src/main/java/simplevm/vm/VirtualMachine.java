package simplevm.vm;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;

import static simplevm.vm.Bytecode.*;

public class VirtualMachine {

    public static class Exception extends java.lang.RuntimeException {
        public Exception(String message) {
            super(message);
        }
    }
    
    // Tracing
    //
    boolean trace = false;
    private void trace(String message) {
        if (trace) {
            System.out.println("(IP " + ip + "):" + message);
        }
    }

    // Dump
    //
    private void dump() {
        System.out.println("SimpleVM DUMP");
        System.out.println("=============");
        System.out.println("IP: " + ip);
        System.out.println("Globals: " + Arrays.toString(globals));
        System.out.println("Working stack (SP " + sp + "): " + Arrays.toString(Arrays.copyOfRange(stack, 0, sp+1)));
        System.out.println("Call stack: ");
        for (int f = frames.size(); f != 0; f--) {
            CallFrame cf = frames.get(f - 1);
            System.out.println("  Call Frame " + (f - 1) + ":");
            System.out.println("  +-- Return Address: " + cf.returnAddress);
            System.out.println("  +-- Locals: " + Arrays.toString(cf.locals));
        }
    }

    // Stack management
    //
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
        trace("---> pushed " + value + "; stack: " + Arrays.toString(Arrays.copyOfRange(stack, 0, sp+1)));
    }
    public int pop() {
        int result = stack[sp--];
        trace("---> popped ; stack: " + Arrays.toString(Arrays.copyOfRange(stack, 0, sp+1)));
        return result;
    }

    public static class CallFrame {
        public int[] locals = null;
        public int returnAddress = -1;
        public CallFrame() {
            // assume a max of 32 locals for now
            locals = new int[32];
        }
    }
    public List<CallFrame> frames = new LinkedList<>();
    public CallFrame fp() { return frames.get(frames.size() - 1); }

    // Globals
    //
    int[] globals = new int[32];
    int[] getGlobals() {
        return globals;
    }

    // Execution
    //
    int ip = 0;
    public void execute(int opcode, int... operands) {
        switch (opcode) {
            case NOP:
                // Do nothing!
                trace("NOP");
                break;
            case DUMP:
                trace("DUMP");
                dump();
                break;
            case TRACE:
                trace = !trace;
                trace("TRACE");
                break;
            case PRINT:
                trace("PRINT");
                System.out.println(pop());
                break;
            case FATAL:
                trace("FATAL");
                throw new Exception("FATAL bytecode executed at " + ip);
            
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
                trace("JMP " + operands[0]);
                ip = operands[0] - 1; // offset for the ip++ below
                break;
            }
            case RJMP:
            {
                trace("RJMP " + operands[0]);
                ip += operands[0] - 1; // offset for the ip++ below
                break;
            }
            case JMPI:
            {
                trace("JMPI");
                int location = pop();
                ip = location - 1; // offset for the ip++ below
                break;
            }
            case RJMPI:
            {
                trace("RJMPI");
                int offset = pop();
                ip += offset - 1; // offset for the ip++ below
                break;
            }
            case JZ:
            {
                trace("JZ " + operands[0]);
                int jump = pop();
                if (jump == 0) { 
                    ip = operands[0] - 1; // offset for the ip++ below
                }
                break;
            }
            case JNZ:
            {
                trace("JNZ " + operands[0]);
                int jump = pop();
                if (jump != 0) { 
                    ip = operands[0] - 1; // offset for the ip++ below
                }
                break;
            }

            // Globals
            //
            case GLOAD:
            {
                trace("GLOAD " + operands[0]);
                push(globals[operands[0]]);
                break;
            }
            case GSTORE:
            {
                trace("GSTORE " + operands[0]);
                globals[operands[0]] = pop();
                break;
            }

            // Functions
            //
            case CALL:
            {
                trace("CALL to " + operands[0]); // go to next instruction
                CallFrame next = new CallFrame();
                next.returnAddress = ip + 2; // take the instruction after this+operand
                frames.add(next);
                ip = operands[0] - 1; // -1 is to offset the "ip++" below

                break;
            }
            case RET:
            {
                CallFrame sf = frames.remove(frames.size() - 1);
                trace("RET (to " + sf.returnAddress + ")");
                if (sf.returnAddress == -1) {
                    // We are returning from the topmost level,
                    // which means our CALL/RETs are imbalanced
                    throw new Exception("Cannot RET from topmost level");
                }
                else {
                    ip = sf.returnAddress - 1; // offset the ip++ below
                }
                break;
            }
            case LOAD:
            {
                trace("LOAD " + operands[0]);
                push(fp().locals[operands[0]]);
                break;
            }
            case STORE:
            {
                trace("STORE " + operands[0]);
                fp().locals[operands[0]] = pop();
                break;
            }

            default:
                throw new Exception("Unrecognized opcode: " + opcode);
        }
    }
    public void execute(int[] code) {
        // We always have at least one CallFrame
        frames.add(new CallFrame());

        for (ip = 0; ip < code.length; )
        {
            switch (code[ip])
            {
                case HALT:
                    trace("HALT at " + ip);
                    return;

                // 0-operand opcodes
                case NOP:
                case TRACE:
                case DUMP:
                case PRINT:
                case FATAL:
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
                case RET:
                    execute(code[ip]);
                    break;
                
                // 1-operand opcodes
                case CONST:
                case JMP:
                case RJMP:
                case JZ:
                case JNZ:
                case GLOAD:
                case GSTORE:
                case CALL:
                case STORE:
                case LOAD:
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
