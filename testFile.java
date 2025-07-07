import java.util.Arrays;

public class Sandbox {
    public char[][] encryptors(String s) {
        char[][] result = new char[4][4];

        // Ensure the string is exactly 16 characters long
        s = s.replaceAll("\\s", ""); // optional: remove whitespace
        if (s.length() < 16) {
            s = String.format("%-16s", s); // pad with spaces if too short
        } else if (s.length() > 16) {
            s = s.substring(0, 16); // trim if too long
        }

        int counter = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = s.charAt(counter++);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Sandbox test = new Sandbox();
        char[][] testcase = test.encryptors("Hell Yeah");
        
        // Print the 2D array
        for (char[] row : testcase) {
            System.out.println(Arrays.toString(row));
        }
    }
}
