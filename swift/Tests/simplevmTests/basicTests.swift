import XCTest
@testable import simplevm

final class basicTests: XCTestCase {
    func testExample() {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct
        // results.
        XCTAssertEqual(simplevm().text, "SimpleVM")
        XCTAssertEqual(simplevm().version, "0.0")
    }

    static var allTests = [
        ("testExample", testExample),
    ]
}
