import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * This class serves as a comprehensive example of JUnit 5 features.
 * It demonstrates core annotations, assertion methods, and assumption methods
 * by testing a simple Calculator class.
 *
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS) allows us to have non-static
 * @BeforeAll and @AfterAll methods, as the test class is instantiated only once.
 */
@DisplayName("JUnit 5 Comprehensive Feature Showcase")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ComprehensiveJUnit5Test {

    // The class under test
    private static class Calculator {
        int add(int a, int b) {
            return a + b;
        }

        int subtract(int a, int b) {
            return a - b;
        }

        int divide(int a, int b) {
            if (b == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            return a / b;
        }

        void longRunningOperation() {
            try {
                Thread.sleep(100); // Simulates a time-consuming task
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Calculator calculator;

    // --- Core Annotations ---

    /**
     * @BeforeAll: Executed once, before any of the tests in this class run.
     * Ideal for setting up expensive resources like database connections.
     * Must be static if the test instance lifecycle is PER_METHOD (the default).
     */
    @BeforeAll
    void setupAll() {
        System.out.println("Setting up resources for all tests...");
    }

    /**
     * @AfterAll: Executed once, after all tests in this class have completed.
     * Used for tearing down resources set up in @BeforeAll.
     */
    @AfterAll
    void tearDownAll() {
        System.out.println("Tearing down resources for all tests.");
    }

    /**
     * @BeforeEach: Executed before each @Test method.
     * Used to reset the state before each test to ensure test isolation.
     */
    @BeforeEach
    void setupEach() {
        System.out.println("-- Setting up for a new test --");
        calculator = new Calculator();
    }

    /**
     * @AfterEach: Executed after each @Test method.
     * Used for cleanup after each test.
     */
    @AfterEach
    void tearDownEach() {
        System.out.println("-- Tearing down after a test --\n");
        calculator = null;
    }

    /**
     * @Test: Marks a method as a test method.
     * @DisplayName: Provides a more descriptive name for the test in reports.
     */
    @Test
    @DisplayName("1. Basic Addition Test")
    void testAddition() {
        System.out.println("Executing: Basic Addition Test");
        // --- Assertion Methods ---
        assertEquals(5, calculator.add(2, 3), "2 + 3 should equal 5");
        assertNotEquals(6, calculator.add(2, 3), "2 + 3 should not equal 6");
    }

    @Test
    @DisplayName("2. Boolean and Nullness Assertions")
    void testBooleanAndNullAssertions() {
        System.out.println("Executing: Boolean and Nullness Assertions");
        assertTrue(5 > 2, "5 should be greater than 2");
        assertFalse(2 > 5, "2 should not be greater than 5");
        assertNotNull(calculator, "Calculator instance should not be null");

        Calculator nullCalculator = null;
        assertNull(nullCalculator, "This calculator instance should be null");
    }

    @Test
    @DisplayName("3. Object Identity Assertion")
    void testSameAndNotSame() {
        System.out.println("Executing: Object Identity Assertion");
        Calculator anotherCalculatorInstance = calculator;
        Calculator differentCalculator = new Calculator();

        assertSame(calculator, anotherCalculatorInstance, "These should be the same object instance");
        assertNotSame(calculator, differentCalculator, "These should be different object instances");
    }

    @Test
    @DisplayName("4. Grouped Assertions with assertAll")
    void testGroupedAssertions() {
        System.out.println("Executing: Grouped Assertions");
        // assertAll executes all nested assertions and reports all failures together.
        assertAll("Calculator Operations",
            () -> assertEquals(4, calculator.add(2, 2)),
            () -> assertEquals(0, calculator.subtract(2, 2)),
            () -> assertEquals(1, calculator.divide(2, 2))
        );
    }

    @Test
    @DisplayName("5. Exception Handling Assertion")
    void testExceptionThrowing() {
        System.out.println("Executing: Exception Handling Assertion");
        // Asserts that the executable throws the specified exception type.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(1, 0);
        });
        assertEquals("Cannot divide by zero", exception.getMessage());

        // Asserts that the executable does NOT throw an exception.
        assertDoesNotThrow(() -> {
            calculator.divide(1, 1);
        });
    }

    @Test
    @DisplayName("6. Timeout Assertion")
    void testTimeout() {
        System.out.println("Executing: Timeout Assertion");
        // Fails if the operation takes longer than the specified duration.
        assertTimeout(Duration.ofMillis(500), () -> {
            calculator.longRunningOperation();
        }, "The operation took too long to complete.");
    }
    
    /**
     * @Disabled: Prevents a test method or class from being executed.
     */
    @Test
    @Disabled("This test is disabled for demonstration purposes.")
    @DisplayName("7. A Disabled Test")
    void disabledTest() {
        fail("This test should not run.");
    }

    // --- Assumption Methods ---

    @Test
    @DisplayName("8. Assumption Methods Showcase")
    void testAssumptions() {
        System.out.println("Executing: Assumption Methods Showcase");
        // assumeTrue: The test proceeds only if this condition is true.
        // Otherwise, the test is aborted (skipped), not failed.
        assumeTrue("CI".equals(System.getenv("ENV")),
            "Aborting test: Not on a CI server.");

        // This part of the test will only run if the assumption above is met.
        System.out.println("This line runs because we are on a CI server (hypothetically).");
        assertEquals(4, calculator.add(2, 2));
    }

    @Test
    @DisplayName("9. assumingThat Demonstration")
    @EnabledOnOs(OS.WINDOWS) // A conditional annotation
    void testAssumingThat() {
        System.out.println("Executing: assumingThat Demonstration (runs only on Windows)");
        // assumingThat: Executes a block of code only if the assumption is true.
        // The rest of the test runs regardless of the assumption's outcome.
        assumingThat(true,
            () -> {
                System.out.println("This block is executed because the assumption is true.");
                assertEquals(10, calculator.add(5, 5));
            }
        );

        System.out.println("This line is always executed.");
        assertEquals(2, calculator.add(1, 1));
    }

    // --- Nested Tests and Tags ---

    /**
     * @Nested: Allows for grouping of tests in a nested class.
     * This helps in organizing tests and expressing relationships between them.
     */
    @Nested
    @DisplayName("Advanced Operations")
    @Tag("advanced") // @Tag: Used to filter tests.
    class AdvancedTests {

        @Test
        @DisplayName("Subtraction Test")
        void testSubtraction() {
            System.out.println("Executing: Subtraction Test (Nested)");
            assertEquals(-1, calculator.subtract(2, 3));
        }
    }
}
