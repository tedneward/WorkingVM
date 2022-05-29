namespace vmtest;

using vm;

[TestClass]
public class MathTests
{
    [TestMethod]
    public void TestADD()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)5,
            Bytecode.CONST, (Bytecode)5,
            Bytecode.ADD,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(10, vm.Stack[0]);
    }
    [TestMethod]
    public void TestSUB()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)15,
            Bytecode.CONST, (Bytecode)5,
            Bytecode.SUB,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(10, vm.Stack[0]);
    }
    [TestMethod]
    public void TestMUL()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)5,
            Bytecode.CONST, (Bytecode)2,
            Bytecode.MUL,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(10, vm.Stack[0]);
    }
    [TestMethod]
    public void TestDIV()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)20,
            Bytecode.CONST, (Bytecode)2,
            Bytecode.DIV,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(10, vm.Stack[0]);
    }
    [TestMethod]
    public void TestMOD()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)30,
            Bytecode.CONST, (Bytecode)20,
            Bytecode.MOD,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(10, vm.Stack[0]);
    }
    [TestMethod]
    public void TestABS()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)30,
            Bytecode.ABS,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(30, vm.Stack[0]);
    }
    [TestMethod]
    public void TestNEG()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)(-10),
            Bytecode.NEG,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(10, vm.Stack[0]);
    }
    [TestMethod]
    public void TestNEGNEG()
    {
        VirtualMachine vm = new VirtualMachine();

        Bytecode[] code = {
            Bytecode.CONST, (Bytecode)(10),
            Bytecode.NEG,
            Bytecode.NEG,
        };
        vm.Execute(code);

        Assert.AreEqual(1, vm.Stack.Length);
        Assert.AreEqual(10, vm.Stack[0]);
    }
}
