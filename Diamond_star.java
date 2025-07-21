public class DiamondPattern {
    public static void main(String[] args) {
        int n = 5; // height of the diamond (middle line has n asterisks)

        // Upper half of the diamond (including middle line)
        for (int i = 1; i <= n; i++) {
            // Print spaces
            for (int j = i; j < n; j++) {
                System.out.print(" ");
            }

            // Print asterisks
            for (int j = 1; j <= (2 * i - 1); j++) {
                System.out.print("*");
            }

            System.out.println();
        }

        // Lower half of the diamond
        for (int i = n - 1; i >= 1; i--) {
            // Print spaces
            for (int j = n; j > i; j--) {
                System.out.print(" ");
            }

            // Print asterisks
            for (int j = 1; j <= (2 * i - 1); j++) {
                System.out.print("*");
            }

            System.out.println();
        }
    }
}
