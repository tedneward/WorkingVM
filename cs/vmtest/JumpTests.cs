namespace vmtest;

using vm;

[TestClass]
public class JumpTests
{
    [TestMethod]
    public void TestJMP()
    {
        VirtualMachine vm = new VirtualMachine();

        // CONST and FATALs here should be bypassed...
        vm.Execute(new Bytecode[] {
            Bytecode.JMP, (Bytecode)6,
            Bytecode.CONST, (Bytecode)12,
            Bytecode.FATAL,
            Bytecode.FATAL,
            Bytecode.NOP
        });

        // ... leaving nothing on the stack
        Assert.AreEqual(0, vm.Stack.Length);
    }
    [TestMethod]
    public void TestJMPI()
    {
        VirtualMachine vm = new VirtualMachine();

        // CONSTs here should be bypassed...
        vm.Execute(new Bytecode[] {
            Bytecode.CONST, (Bytecode)7,
            Bytecode.JMPI,
            Bytecode.CONST, (Bytecode)12,
            Bytecode.CONST, (Bytecode)27,
            Bytecode.NOP
        });

        // ... leaving nothing on the stack
        Assert.AreEqual(0, vm.Stack.Length);
    }

    [TestMethod]
    public void TestLotsofJumps() {
        VirtualMachine vm = new VirtualMachine();
        vm.Execute(new Bytecode[] {
            /* 0*/ Bytecode.TRACE,
            /* 1*/ Bytecode.JMP, (Bytecode)4,  // bypass FATAL opcode
            /* 3*/ Bytecode.FATAL,
            /* 4*/ Bytecode.JMP, (Bytecode)7,
            /* 6*/ Bytecode.FATAL,
            /* 7*/ Bytecode.JMP, (Bytecode)10,
            /* 9*/ Bytecode.FATAL,
            /*10*/ Bytecode.NOP
        });

        Assert.AreEqual(0, vm.Stack.Length);
    }
    [TestMethod]
    public void TestCountdownImplementation() {
        VirtualMachine vm = new VirtualMachine();
        vm.Execute(new Bytecode[] {
            // countdown from 13 to 10
            /* 0*/ Bytecode.TRACE,                  // tracing on/off
            /* 1*/ Bytecode.CONST, (Bytecode)13,    // store 13 (starting count)...
            /* 3*/ Bytecode.GSTORE, (Bytecode)0,    // ... stored into globals[0]
            /* 5*/ Bytecode.GLOAD, (Bytecode)0,     // load globals[0]/count
            /* 7*/ Bytecode.PRINT,                  // print
            /* 8*/ Bytecode.GLOAD, (Bytecode)0,     // globals[0]/count
            /*10*/ Bytecode.CONST, (Bytecode)10,    // 10
            /*12*/ Bytecode.EQ,                     // globals[0]/count == 0 ?
            /*13*/ Bytecode.JNZ, (Bytecode)24,      // jump to return
            /*15*/ Bytecode.GLOAD, (Bytecode)0,     // globals[0] = globals[0] - 1
            /*17*/ Bytecode.CONST, (Bytecode)1,     // 1
            /*19*/ Bytecode.SUB,                    // subtract (rhs: 1, lhs: globals[0])
            /*20*/ Bytecode.GSTORE, (Bytecode)0,    // store result back to globals[0]/count
            /*22*/ Bytecode.JMP, (Bytecode)5,       // jump to top of loop
        });

        Assert.AreEqual(0, vm.Stack.Length);
        Assert.AreEqual(10, vm.Globals[0]);
    }
}
