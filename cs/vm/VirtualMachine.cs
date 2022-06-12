namespace vm;

public enum Bytecode 
{
    NOP,
    DUMP,
    TRACE,
    PRINT,
    HALT,
    FATAL,

    // Stack opcodes
    CONST,
    POP,

    // Math opcodes (binary)
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,

    // Math opcodes (unary)
    ABS,
    NEG,

    // Comparison
    EQ,
    NEQ,
    GT,
    LT,
    GTE,
    LTE,

    // Branching opcodes
    JMP,
    JMPI,
    RJMP,
    RJMPI,
    JZ,
    JNZ,

    // Globals
    GSTORE,
    GLOAD,

    // Procedures/locals
    CALL,
    RET,
    LOAD,
    STORE
}

public class VirtualMachine
{
    // Tracing
    //
    bool trace = false;
    private void Trace(string message)
    {
        if (trace)
            Console.WriteLine("TRACE: {0}", message);
    }

    // Stack management
    //
    int SP = -1; // points to the current top of stack
    int[] stack = new int[100];
    public int[] Stack { get { return stack.Take(SP+1).ToArray(); } }
    public void Push(int operand)
    {
        Trace("Push: " + operand);
        stack[++SP] = operand;
        Trace(" -->  Stack: " + String.Join(",", stack));
    }
    public int Pop()
    {
        Trace("Pop");
        int result = stack[SP--];
        Trace(" -->  Stack: " + String.Join(",", stack));
        return result;
    }

    // Globals
    //
    public int[] globals = new int[256];
    public int[] Globals { get { return globals; } }

    // Locals and CallFrames
    //
    public class CallFrame
    {
        public int[] Locals { get; private set; }
        public int ReturnAddress { get; set; }
        public CallFrame() {
            // assume a max of 32 locals for now
            Locals = new int[32];
        }
    }
    List<CallFrame> Frames = new List<CallFrame>();
    CallFrame FP()
    {
        return Frames[Frames.Count - 1];
    }

    // Execution
    //
    int IP = -1;
    public void Execute(Bytecode opcode, params int[] operands)
    {
        switch (opcode)
        {
                case Bytecode.NOP:
                    // Do nothing!
                    Trace("NOP");
                    break;
                case Bytecode.DUMP:
                    Trace("DUMP");
                    Console.WriteLine("VirtualMachine DUMP:");
                    Console.WriteLine("  SP: {0}, stack: [{1}]", SP, String.Join(",", stack));
                    break;
                case Bytecode.TRACE:
                    trace = !trace;
                    Trace("TRACE " + trace);
                    break;
                case Bytecode.PRINT:
                    Trace("PRINT");
                    Console.WriteLine(Pop());
                    break;
                case Bytecode.HALT:
                    Trace("HALT");
                    return;
                case Bytecode.FATAL:
                    Trace("FATAL");
                    throw new Exception(String.Format("FATAL exception thrown; IP {0}",IP));
                case Bytecode.CONST:
                    int operand = operands[0];
                    Trace("CONST " + operand);
                    Push(operand);
                    break;
                case Bytecode.POP:
                    Trace("POP");
                    // throw away returned value
                    Pop();
                    break;
                case Bytecode.ADD:
                {
                    Trace("ADD");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs + rhs);
                    break;
                }
                case Bytecode.SUB:
                {
                    Trace("SUB");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs - rhs);
                    break;
                }
                case Bytecode.MUL:
                {
                    Trace("MUL");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs * rhs);
                    break;
                }
                case Bytecode.DIV:
                {
                    Trace("DIV");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs / rhs);
                    break;
                }
                case Bytecode.MOD:
                {
                    Trace("MOD");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs % rhs);
                    break;
                }
                case Bytecode.ABS:
                {
                    Trace("ABS");
                    int val = Pop();
                    Push(Math.Abs(val));
                    break;
                }
                case Bytecode.NEG:
                {
                    Trace("NEG");
                    int val = Pop();
                    Push(-val);
                    break;
                }
                
                // Comparison ops
                case Bytecode.EQ:
                {
                    Trace("EQ");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs == rhs ? 1 : 0);
                    break;
                }
                case Bytecode.NEQ:
                {
                    Trace("NEQ");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs != rhs ? 1 : 0);
                    break;
                }
                case Bytecode.GT:
                {
                    Trace("GT");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs > rhs ? 1 : 0);
                    break;
                }
                case Bytecode.LT:
                {
                    Trace("LT");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs < rhs ? 1 : 0);
                    break;
                }
                case Bytecode.GTE:
                {
                    Trace("GTE");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs >= rhs ? 1 : 0);
                    break;
                }
                case Bytecode.LTE:
                {
                    Trace("LTE");
                    int rhs = Pop();
                    int lhs = Pop();
                    Push(lhs <= rhs ? 1 : 0);
                    break;
                }

                // Branching ops
                case Bytecode.JMP:
                {
                    Trace("JMP " + operands[0]);
                    IP = operands[0] - 1; // offset for IP++ below
                    break;
                }
                case Bytecode.RJMP:
                {
                    Trace("RJMP " + operands[0]);
                    IP += operands[0] - 1; // offset for IP++ below
                    break;
                }
                case Bytecode.JMPI:
                {
                    int location = Pop();
                    Trace("JMPI " + location);
                    IP = location - 1; // offset for IP++ below
                    break;
                }
                case Bytecode.RJMPI:
                {
                    int offset = Pop();
                    Trace("RJMPI " + offset);
                    IP += offset - 1; // offset for IP++ below
                    break;
                }
                case Bytecode.JZ:
                {
                    Trace("JZ " + operands[0]);
                    if (Pop() == 0) {
                        IP = operands[0] - 1; // offset for IP++ below
                    }
                    break;
                }
                case Bytecode.JNZ:
                {
                    Trace("JNZ " + operands[0]);
                    if (Pop() != 0) {
                        IP = operands[0] - 1; // offset for IP++ below
                    }
                    break;
                }

                // Globals
                case Bytecode.GSTORE:
                {
                    int index = operands[0];
                    globals[index] = Pop();
                    break;
                }
                case Bytecode.GLOAD:
                {
                    int index = operands[0];
                    Push(globals[index]);
                    break;
                }

                // Functions
                case Bytecode.CALL:
                {
                    Trace("CALL to " + operands[0]); // go to next instruction
                    CallFrame next = new CallFrame();
                    next.ReturnAddress = IP + 2; // take the instruction after this+operand
                    Frames.Add(next);
                    IP = operands[0] - 1; // -1 is to offset the "ip++" below

                    break;
                }
                case Bytecode.RET:
                {
                    CallFrame sf = Frames[Frames.Count - 1];
                    Frames.RemoveAt(Frames.Count - 1);
                    Trace("RET (to " + sf.ReturnAddress + ")");
                    IP = sf.ReturnAddress - 1; // offset the ip++ below
                    break;
                }
                case Bytecode.LOAD:
                {
                    Trace("LOAD " + operands[0]);
                    Push(FP().Locals[operands[0]]);
                    break;
                }
                case Bytecode.STORE:
                {
                    Trace("STORE " + operands[0]);
                    FP().Locals[operands[0]] = Pop();
                    break;
                }

                default:
                    throw new Exception("Unrecognized opcode: " + opcode);
        }
    }
    public void Execute(Bytecode[] code)
    {
        // We always have at least one call frame
        Frames.Add(new CallFrame());

        for (IP = 0; IP < code.Length; )
        {
            Bytecode opcode = code[IP];
            switch (opcode)
            {
                // 0-operand opcodes
                case Bytecode.NOP:
                case Bytecode.DUMP:
                case Bytecode.TRACE:
                case Bytecode.PRINT:
                case Bytecode.FATAL:
                case Bytecode.POP:
                case Bytecode.ADD:
                case Bytecode.SUB:
                case Bytecode.MUL:
                case Bytecode.DIV:
                case Bytecode.MOD:
                case Bytecode.ABS:
                case Bytecode.NEG:
                case Bytecode.EQ:
                case Bytecode.NEQ:
                case Bytecode.GT:
                case Bytecode.LT:
                case Bytecode.GTE:
                case Bytecode.LTE:
                case Bytecode.JMPI:
                case Bytecode.RJMPI:
                case Bytecode.RET:
                    Execute(opcode);
                    break;

                // 1-operand opcodes
                case Bytecode.CONST:
                case Bytecode.JMP:
                case Bytecode.RJMP:
                case Bytecode.JZ:
                case Bytecode.JNZ:
                case Bytecode.GSTORE:
                case Bytecode.GLOAD:
                case Bytecode.CALL:
                case Bytecode.STORE:
                case Bytecode.LOAD:
                    int operand = (int)code[++IP];
                    Execute(opcode, operand);
                    break;

                // 2-operand opcodes

                // Special handling to bail out early
                case Bytecode.HALT:
                    return;

                // Unrecognized opcode
                default:
                    throw new Exception("Unrecognized opcode: " + code[IP]);
            }
            IP++;
        }
    }
}
