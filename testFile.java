import java.util.Arrays;
public class Sandbox {
    public char[][] encryptors(String s) {
        char[][] result = new char[4][4];

        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                System.out.println(s.charAt(j));
                result[i][j] = s.charAt(counter);
                counter++;
                
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Sandbox test = new Sandbox();
        char[][] testcase = test.encryptors("Hell Yeah");
        System.out.println(Arrays.toString(testcase));
    }
}
