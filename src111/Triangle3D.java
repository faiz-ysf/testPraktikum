import java.util.Arrays;

public class Triangle3D {

    public static int[][] createZigzagTriangle(int n) {
        if (n < 0) {
            return new int[0][];
        }
        int[][] triangle = new int[n][];
        int size = 1;
        int i = 0;

        // Buat bentuk segitiga zigzag (gergaji)
        while (i < n) {
            for (int j = 0; j < size && i + j < n; j++) {
                triangle[i + j] = new int[j + 1];
            }
            i += size;
            size++;
        }
        return triangle;
    }

    public static void fillTriangle(int[][] triangle) {
        if (triangle == null || triangle.length == 0) {
            return;
        }
        int maxCols = 0;
        for (int[] row : triangle) {
            if (row != null && row.length > maxCols) {
                maxCols = row.length;
            }
        }

        int val = 0;
        for (int col = 0; col < maxCols; col++) {
            for (int row = 0; row < triangle.length; row++) {
                if (triangle[row] != null && col < triangle[row].length) {
                    triangle[row][col] = val++ % 10;
                }
            }
        }
    }

    public static void printTriangle(int[][] triangle) {
        for (int[] row : triangle) {
            if (row != null) {
                for (int num : row) {
                    System.out.print(num + " ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int n = 15; // jumlah baris, bisa diubah
        int[][] triangle = createZigzagTriangle(n);
        fillTriangle(triangle);
        printTriangle(triangle);
    }
}