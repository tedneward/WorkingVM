# WorkingVM
A testbed for the VMImpl workshop

# Steps
The steps to the workshop are as follows:

## Step 0: Infrastructure
`git checkout step-0`

Our VirtualMachine needs some basic infrastructure to get started. Our VM will be an extremely simple stack-based machine, so as to keep the infrastructure needs lightweight and straightforward. We will work solely wth integers (no floats, no strings, no booleans, etc) to keep things simple, and we will work to start by handling the simplest opcodes, which either do nothing (NOP), or give us some diagnostic insight and/or control.

There are some tests already defined in the respective test projects; if you use different names than the ones described here, refactor the tests accordingly, and by all means, feel free to add a few more tests if you feel the need.

* We want to begin by implementing the world's simplest opcode: `NOP`, which literally does nothing. Create a type that will contain a number of integer constant values called `Bytecode`.

    > **C#, C++**: This is most easily done with an `enum`, since enumerations in both languages are backed by an integer type.
    > **Java**: This is most easily done with a series of `public static final int` values, since Java enumerations are object instances, not integer-backed values.

    Within that enumeration, define `NOP` and set its value to 0. (Most bytecode and CPU instructions set `NOP` to 0 for a variety of reasons both historical and practical.)

* The processor cycle (fetch-decode-execute) is easier to understand if we break it apart. We'll worry about fetch and decode later; create a method, `execute`/`Execute` (depending on your language's naming conventions) that takes a single `Bytecode`/`int` parameter (the `opcode`) and a variable-length array of integers for `operands`. Within this method, you will examine the opcode and (at some point) any operands that are required to execute it. Put some error-handling in here to throw an exception if the opcode is not recognized. Make this method publicly accessible so we can call it from tests.
    * For a `NOP` value, do nothing.
    * Implement a `DUMP` opcode (add it to `Bytecode` and to our `execute`/`Execute` method) that dumps the current state of the VM to console (or logfile, if you want to build one out).
    * Implement a `TRACE` opcode that flips the status of a private `trace` boolean field (from true to false or false to true). Build a private method that takes a message and prints it to console or log file if `trace` is set to true. Use this method to help trace execution within the VM while writing/testing new opcodes through this workshop.

* Given that this is a stack machine, we need an execution stack on which to push and pop values that will be the input and output for our various opcodes. We will use a fixed-size stack of integers, and we will "push" elements ("grow the stack") towards 100; the first element will be pushed at array index 0, the second at array index 1, and so on. For simplicity's sake, we will not worry about growing the stack or shrinking it. We'll want a reference to the "top" of the stack, and for testing purposes we'll want to be able to see the stack from the outside of the VM.

    > **NOTE**: If we truly didn't care about performance, we could use a higher-level abstract data type class, such as Stack class, to represent our execution stack, but this approach is more accurate to what a virtual machine actually does.

    * Create an array of 100 integers and call it `stack`. This will be the raw stack. 
    * Create an integer that points to the current index of the top of the stack; this is our "stack pointer", so call it `SP`.
    * Create two public methods, `push`/`Push` (which takes an integer and returns nothing) and `pop`/`Pop` (which takes nothing and returns an integer) that push or pop the value at the top of the stack respectively. Make sure `SP` gets incremented or decremented correctly with each push or pop.
    * Create a public method/property (`getStack()`/`Stack`) that returns a copy of the current contents of the stack. For easier diagnostics, make sure to only return the part of the stack that is in use--so if the current SP is 2, only return elements 0 through 2 of the total stack. If the current SP is -1, then return an empty array (the stack is empty).

* Now let's put two of the core stack machine opcodes into place: `CONST`, which takes one operand containing a value to push onto the stack, and `POP`, which pops a value off the stack (and throws it away). Note that `CONST` has one quirk to it, in that the value to push will be embedded in the code itself, so it will be masquerading as a `Bytecode` value. This is, in many respects, the "decode" part of the fetch-decode-execute cycle. (For most languaes, enumeration types are backed by an integer, so you'll just need to cast from `Bytecode` to `int` and back again to keep the compilers happy.)

    > **NOTE**: This means that any given collection of code will actually contain values that aren't entirely code, and this is true of all assembly languages. In many more optimized bytecode or CPU sets, these values can be "packed" together with the opcode value to allow for more efficient storage, and thus "unpacking" these values becomes a core part of the "decode" step. To keep things simple, we will not be exploring this packing/unpacking behavior.

    Make sure to write tests that ensure the stack works correctly. You shouldn't need to be too exhaustive--once you've verified that pushes advance the SP and pops reduce it, and that values appear on the stack and then are gone again, we can move on. (In a production implementation, )

* Lastly, let's create a method that will take an array of `Bytecode` and execute them in sequence, until we run out of them. Call this method `execute`/`Execute` as well, and loop through the array of bytecode, extracting additional operands as necessary (only `CONST` will need to do this so far) and passing them to the single-opcode version of `execute`/`Execute` you wrote earlier.

* *(Optional)* Although we won't get to needing it in this workshop, most stack-based virtual machines have another stack-manipulation instruction called `DUP` that pops the current top of the stack, then pushes it twice. (It is frequently used when a local variable is used as part of a procedure call, since the values passed on the stack are consumed by the called procedure.) Implement it (and some tests for it) as a test for understanding.

## Step 1: Calculations
`git checkout step-1`

With basic diagnostics and stack manipulation in place, we can implement some mathematical operations. We have two different kinds of mathematical operations: binary operations, which take two values to carry out, and unary operations, which require only one. `ADD`, `SUB`, `MUL`, `DIV`, and `MOD` are all binary operations, taking two values; for unary operations we will focus only on two: `ABS`, which will figure the absolute value, and `NEG`, which will convert a positive value to negative, and vice versa.

Unary operations will take one parameter off the stack, operate on it, and push the result back to the stack.

Binary operations will take two parameters off the stack, operate on them, and push the result to the stack. Because `SUB`, `DIV`, and `MOD` are position-sensitive (`5 - 2` is a different operation than `2 - 5`), we need a convention for passing parameters so we can know which is the left-hand-side and which is the right. Either we can pass the parameters "left-to-right" (which means left is bottommost on the stack, and right is the top), or the opposite, "right-to-left" (which means left is at the top and right on the bottom). There's strong arguments for either approach; this workshop assumes we will take a "left-to-right" approach (mostly because it's easier for us when writing the raw bytecode).

When these seven opcodes are all in place (and the tests pass), we're ready to move on to the next step.

> **NOTE**: Many of you will recognize that we have effectively implemented a Reverse-Polish Notation (RPN) calculator.

## Step 2: Comparisons and Branching
`git checkout step-2`

Any programmatic environment needs to have the ability to evaluate values and take differing actions depending on the result of that comparison. This means we have two need categories of bytecode we need to implement: comparison operations, which will take values off the stack, evaluate them, and push a result (1 for true, 0 for false) back onto the stack, and branching operations, will will change the instruction pointer to tell the virtual machine to execute a different bytecode instruction next.

As we will see, it is often convenient (and common) to combine comparison and branching operations together into single opcodes, and/or to have multiple varieties of comparison operations. We will start with fundamentals, and then build out some convenience opcodes from there.

* The simplest comparison operation is equality--take two values off the stack, and if they are equal, push a "1" onto the stack, otherwise push "0". Call the opcode `EQ`. While you're at it, implement the inverse operation, `NEQ`, to test for not-equals.
* The next-simplest comparisons are the relative ones: greater-than (`GT`) and less-than (`LT`). Again, like `SUB`, these require positional sensitivity; `1 < 2` is different from `1 > 2`.
* Similar sorts of comparison operations are combinations of these: greater-than-or-equals (`GTE`), less-than-or-equals (`LTE`), and so on.

This is a pretty good collection of comparison opcodes. Next let's do some branching opcodes, the easiest of which is `JMP` (an unconditional goto), which will take one operand: the address to jump to. In our simplified bytecode set, that "address" will be the index of the instruction in the array of bytecode, and will be passed in as an operand to the opcode. Thus, `JMP 6` means to jump to the 6th element in the bytecode array.

> **NOTE**: The absolute nature of the jump target allows us to jump to an operand rather than an opcode--for example, consider the sequence "CONST 1 JMP 2", which would push a 0 onto the stack, then jump to the second element in the array (1), and execute it as if it were bytecode (which is the DUMP instruction) before continuing on and JMPing again... This is, in fact, doable under a number of assembly languages, and for a time was considered a hallmark of the master programmer to be able to do so and have it make sense.

Once that's done...

* Implement `JZ`, a jump-if-zero bytecode, which combines a `EQ 0` with a `JMP`. It should take one operand (the index to jump to if the top value on the stack is zero).
* Implement `JNZ`, a jump-if-not-zero bytecode, which jumps if the top-of-the-stack is NOT zero.

We can also implement "indirect" jumps, which jumps to a location as specified at runtime rather than at the time the bytecode was written ("compile-time").

* Implement `JMPI`, an "indirect jump", which jumps to the location specified by the value at the top of the stack. So `CONST 0 JMPI` jumps back to the very start of the bytecode (location 0).

Sometimes calculating the absolute positions of where to jump is hard, so it's easier to have the bytecode take jump target locations that are relative to the current position.)

* Implement `RJMP`, which is a "relative jump", which jumps a number of bytecode positions (positive or negative) from its current location. So `RJMP -1` would jump back to the instruction previous to the RJMP, and `RJMP 1` would jump to the next instruction (which would really accomplish the exact same thing as doing nothing). Keep in mind that `RJMP 0` would effectively be an infinite loop--we just keep jumping to the same instruction.

## Step 3: Add locals and globals

