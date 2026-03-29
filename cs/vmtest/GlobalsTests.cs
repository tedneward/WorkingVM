namespace vmtest;

using vm;

[TestClass]
public class GlobalsTests
{
    [TestMethod]
    public void TestGlobalLoad()
    {
        VirtualMachine vm = new VirtualMachine();
        vm.Globals[0] = 27;
        vm.Execute(new Bytecode[] {
            Bytecode.GLOAD, (Bytecode)0
        });

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(27, vm.Stack[0]);
    }
    [TestMethod]
    public void TestGlobalStore()
    {
        VirtualMachine vm = new VirtualMachine();
        vm.Execute(new Bytecode[] {
            Bytecode.CONST, (Bytecode)27,
            Bytecode.GSTORE, (Bytecode)0
        });

        Assert.AreEqual(0, vm.Stack.Length);
        Assert.AreEqual(27, vm.Globals[0]);
    }

    [TestMethod]
    public void TestGlobals()
    {
        VirtualMachine vm = new VirtualMachine();
        vm.Execute(new Bytecode[] {
            Bytecode.CONST, (Bytecode)27,
            Bytecode.GSTORE, (Bytecode)0,
            Bytecode.GLOAD, (Bytecode)0,
            Bytecode.GLOAD, (Bytecode)0,
            Bytecode.ADD,
            Bytecode.GSTORE, (Bytecode)1
        });

        // Stack should be empty
        Assert.AreEqual(0, vm.Stack.Length);

        // Global 0 should have our CONST 27 in it
        Assert.AreEqual(27, vm.Globals[0]);
        // Global 1 should have 27 + 27
        Assert.AreEqual(27 + 27, vm.Globals[1]);
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
