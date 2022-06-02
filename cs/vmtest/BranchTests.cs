namespace vmtest;

using vm;

[TestClass]
public class BranchTests
{
    [TestMethod]
    public void TestJMP()
    {
        VirtualMachine vm = new VirtualMachine();

        // CONSTs here should be bypassed...
        Bytecode[] code = {
            Bytecode.JMP, (Bytecode)6,
            Bytecode.CONST, (Bytecode)12,
            Bytecode.CONST, (Bytecode)27,
            Bytecode.NOP
        };
        vm.Execute(code);

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
}
