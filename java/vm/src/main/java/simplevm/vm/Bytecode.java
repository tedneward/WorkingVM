package simplevm.vm;

public enum Bytecode {
    NOP(0),
    DUMP(1),
    TRACE(2),

    // Stack manipulation
    CONST(10),
    POP(11),

    // Placeholder
    FINAL(255);

    private final int raw;

    Bytecode(int op) {
        this.raw = op;
    }
    public int raw() { return raw; }

    public static Bytecode fromValue(int any) { return new Bytecode(any); }
}
