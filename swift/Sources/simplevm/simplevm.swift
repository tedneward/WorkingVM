struct simplevm {
    var text = "SimpleVM"
    var version = "0.0"
}

public enum Bytecode {
    case NOP
    case DUMP
    case TRACE
}

public class VirtualMachine {

    // Tracing
    //
    var trace = false
    func trace(_ message: String...) {
        if trace {
            print("TRACE: \(message.joined())")
        }
    }

    // Diagnostic dump
    //
    func dump() {
        print("SimpleVM v0.0")
        print("=============")
    }

    var sp = -1
    var stack = [Int](repeating: 0, count: 100)
    var Stack : [Int] {
        get { return Array(stack[0 ..< (sp + 1)]) }
    }
    public func push(_ value: Int) { 
        trace("  ----> pushed \(value)")
        sp += 1
        stack[sp] = value
    }
    public func pop() -> Int { 
        let ret = stack[sp]
        sp -= 1
        trace("  ----> popped \(ret)")
        return ret
    }

    public func execute(_ opcode: Bytecode, _ operands: Int...) {

    }
    public func execute(_ code: [Bytecode]) {

    }
}
