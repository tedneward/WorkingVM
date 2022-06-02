package simplevm.vm;

public class Bytecode {
    public static final int NOP = 0;
    public static final int DUMP = 1;
    public static final int TRACE = 2;

    // Stack manipulation
    public static final int CONST = 10;
    public static final int POP = 11;

    // Maths
    public static final int ADD = 20;
    public static final int SUB = 21;
    public static final int MUL = 22;
    public static final int DIV = 23;
    public static final int MOD = 24;
    public static final int ABS = 25;
    public static final int NEG = 26;

    // Comparison
    public static final int EQ = 30;
    public static final int NEQ = 31;
    public static final int GT = 32;
    public static final int LT = 33;
    public static final int GTE = 34;
    public static final int LTE = 35;

    // Branching
    public static final int JMP = 40;
    public static final int RJMP = 41;
    public static final int JMPI = 42;
    public static final int RJMPI = 43;
    public static final int JZ = 44;
    public static final int JNZ = 45;

    // Some utility methods
}
