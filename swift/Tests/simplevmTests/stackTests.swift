import XCTest
@testable import simplevm

final class stackTests: XCTestCase {
    func testEmptyStack() {
        let vm = VirtualMachine()

        XCTAssertEqual(0, vm.Stack.count)
    }

    func testPushPop() {
        let vm = VirtualMachine()

        vm.push(27)
        XCTAssertEqual(1, vm.Stack.count)
        XCTAssertEqual(27, vm.Stack[0])

        let result = vm.pop()
        XCTAssertEqual(27, result)
        XCTAssertEqual(0, vm.Stack.count)
    }

    static var allTests = [
        ("testEmptyStack", testEmptyStack),
    ]
}
