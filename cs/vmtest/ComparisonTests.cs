namespace vmtest;

using vm;

[TestClass]
public class ComparisonTests
{
    [TestMethod]
    public void TestEQ()
    {
        VirtualMachine vm = new VirtualMachine();

        vm.Execute(new Bytecode[] {
            Bytecode.CONST, (Bytecode)12,
            Bytecode.CONST, (Bytecode)27,
            Bytecode.EQ
        });

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(0, vm.Stack[0]); // 12 != 27
    }
    [TestMethod]
    public void TestNEQ()
    {
        VirtualMachine vm = new VirtualMachine();

        vm.Execute(new Bytecode[] {
            Bytecode.CONST, (Bytecode)12,
            Bytecode.CONST, (Bytecode)27,
            Bytecode.NEQ
        });

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(1, vm.Stack[0]); // 12 != 27
    }
}
