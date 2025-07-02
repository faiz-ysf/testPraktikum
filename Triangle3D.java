public class Triangle3D {
    public static void main(String[] args) {
        int n = 15; // jumlah baris, bisa diubah
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

        // isi angka secara vertikal (ke bawah)
        int maxCols = 0;

        // Cari jumlah kolom maksimum (dari panjang array terpanjang)
        for (int[] row : triangle) {
            if (row.length > maxCols) maxCols = row.length;
        }

        int val = 0;

        // Isi berdasarkan kolom ke bawah
        for (int col = 0; col < maxCols; col++) {
            for (int row = 0; row < triangle.length; row++) {
                if (col < triangle[row].length) {
                    triangle[row][col] = val++ % 10;
                }
            }
        }

        // printMatrix
        for (int[] row : triangle) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
