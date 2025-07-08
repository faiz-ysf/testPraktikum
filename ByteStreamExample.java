import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * This class demonstrates how to use ByteArrayInputStream to read data
 * from a byte array as if it were an input stream. This is useful for
 * testing methods that expect an InputStream or for processing data
 * that is already held in memory as a byte array.
 */
public class ByteStreamExample {

    public static void main(String[] args) {
        // 1. Arrange: Prepare the data source.
        // We start with a simple String and convert it into a byte array.
        String sourceData = "Hello, Byte Stream World!";
        byte[] sourceBytes = sourceData.getBytes();

        // 2. Act: Create the stream and process it.
        // We use a try-with-resources statement to ensure the stream is automatically
        // closed after use, which is a best practice for handling I/O resources.
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceBytes)) {

            System.out.println("Reading data from ByteArrayInputStream byte by byte...");
            int byteData;

            // The read() method returns the next byte of data, or -1 if the end
            // of the stream has been reached.
            while ((byteData = inputStream.read()) != -1) {
                // 3. Assert/Verify: We print the character to the console.
                // In a real application, you might perform some processing here.
                System.out.print((char) byteData);
            }
            System.out.println("\n\nFinished reading the stream.");

        } catch (IOException e) {
            // This catch block would handle any I/O errors, though it's unlikely
            // with a ByteArrayInputStream which operates in memory.
            e.printStackTrace();
        }

        // --- Another example: Reading into a buffer ---
        System.out.println("\n--- Reading data into a buffer ---");
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceBytes)) {
            // Create a buffer to hold chunks of data.
            byte[] buffer = new byte[8];
            int bytesRead;

            System.out.println("Reading data in chunks of " + buffer.length + " bytes:");
            // The read(byte[]) method reads up to buffer.length bytes into the buffer
            // and returns the number of bytes read, or -1 at the end of the stream.
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // Convert the chunk of bytes read into a String for display.
                String chunk = new String(buffer, 0, bytesRead);
                System.out.println("Read chunk: \"" + chunk + "\" (" + bytesRead + " bytes)");
            }
            System.out.println("\nFinished reading with a buffer.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}