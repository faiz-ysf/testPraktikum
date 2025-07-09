import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;

/**
 * This class demonstrates the usage of JUnit 4 annotations, assertions,
 * and common Java I/O streams for testing purposes.
 */
public class JUnitAndIoExampleTest {

    // A stream to capture output from System.out
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    // The original System.out PrintStream
    private final PrintStream originalOut = System.out;

    /**
     * @Before: This method runs before each @Test.
     * Here, we redirect System.out to our byte array stream to capture console output.
     */
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    /**
     * @After: This method runs after each @Test.
     * We restore the original System.out stream to avoid side effects between tests.
     */
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Demonstrates basic assertions: assertEquals, assertTrue, assertFalse, assertNotNull, assertNull.
     */
    @Test
    public void testBasicAssertions() {
        String testString = "JUnit";
        String nullString = null;

        assertEquals("Expected strings to be equal", "JUnit", testString);
        assertTrue("Value should be true", 5 > 3);
        assertFalse("Value should be false", 3 > 5);
        assertNotNull("Object should not be null", testString);
        assertNull("Object should be null", nullString);
    }

    /**
     * Demonstrates object identity assertions: assertSame and assertNotSame.
     */
    @Test
    public void testObjectIdentity() {
        String s1 = "hello";
        String s2 = "hello"; // Java often pools string literals, so s1 and s2 might be the same object.
        String s3 = new String("hello"); // Guaranteed to be a new object.

        // assertEquals would pass for s1 and s3, as their contents are equal.
        assertEquals(s1, s3);

        // assertSame fails for s1 and s3 because they are different objects in memory.
        assertNotSame("s1 and s3 should be different objects", s1, s3);
        // Depending on JVM's string pooling, s1 and s2 might be the same.
        // For this example, let's compare an object to itself.
        assertSame("An object should be the same as itself", s1, s1);
    }

    /**
     * Demonstrates assertArrayEquals for comparing array contents.
     */
    @Test
    public void testArrayEquality() {
        char[] expected = {'J', 'U', 'n', 'i', 't'};
        char[] actual = "JUnit".toCharArray();
        // Note: assertEquals(expected, actual) would fail because arrays are objects
        // and these are two different object instances.
        assertArrayEquals("Arrays should have the same content", expected, actual);
    }

    /**
     * Demonstrates testing for expected exceptions using the @Test(expected=...) attribute.
     */
    @Test(expected = ArithmeticException.class)
    public void testExceptionThrowing() {
        System.out.println("This test expects an ArithmeticException to be thrown.");
        int result = 10 / 0;
        fail("This line should not be reached if the exception was thrown.");
    }

    /**
     * Demonstrates reading from a ByteArrayInputStream.
     */
    @Test
    public void testByteArrayInputStream() throws IOException {
        String inputData = "Hello, Stream!";
        byte[] inputBytes = inputData.getBytes();

        // Create a stream from our byte array
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes)) {
            assertEquals("Available bytes should match input length", inputData.length(), inputStream.available());

            StringBuilder result = new StringBuilder();
            int byteRead;
            while ((byteRead = inputStream.read()) != -1) {
                result.append((char) byteRead);
            }

            assertEquals("The read content should match the original data", inputData, result.toString());
        }
    }

    /**
     * Demonstrates writing to a PrintStream and verifying the output
     * using a ByteArrayOutputStream. This test relies on the @Before and @After methods.
     */
    @Test
    public void testOutputStreamAndPrintStream() {
        // This is the class/method we want to test
        class Greeter {
            public void sayHello() {
                System.out.println("Hello, World!");
            }
        }

        // Act: Call the method that prints to the console
        new Greeter().sayHello();

        // Assert: Check if the captured output is what we expect.
        // Note: println adds a line separator, which varies by OS.
        String expectedOutput = "Hello, World!" + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }
}