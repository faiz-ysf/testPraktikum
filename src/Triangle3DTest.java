import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class Triangle3DTest {

    @Test
    public void testCreateZigzagTriangle() {
        int[][] expected = new int[5][];
        expected[0] = new int[1];
        expected[1] = new int[1];
        expected[2] = new int[2];
        expected[3] = new int[1];
        expected[4] = new int[2];

        assertArrayEquals(expected, Triangle3D.createZigzagTriangle(5));
    }

    @Test
    public void testFillTriangle() {
        int[][] triangle = new int[5][];
        triangle[0] = new int[1];
        triangle[1] = new int[1];
        triangle[2] = new int[2];
        triangle[3] = new int[1];
        triangle[4] = new int[2];

        Triangle3D.fillTriangle(triangle);

        int[][] expected = new int[5][];
        expected[0] = new int[]{0};
        expected[1] = new int[]{1};
        expected[2] = new int[]{2, 5};
        expected[3] = new int[]{3};
        expected[4] = new int[]{4, 6};

        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], triangle[i]);
        }
    }

    @Test
    public void testCreateZigzagTriangleWithZero() {
        int[][] expected = new int[0][];
        assertArrayEquals(expected, Triangle3D.createZigzagTriangle(0));
    }

    @Test
    public void testCreateZigzagTriangleWithNegative() {
        int[][] expected = new int[0][];
        assertArrayEquals(expected, Triangle3D.createZigzagTriangle(-5));
    }

    @Test
    public void testFillTriangleWithEmpty() {
        int[][] triangle = new int[0][];
        Triangle3D.fillTriangle(triangle);
        int[][] expected = new int[0][];
        assertArrayEquals(expected, triangle);
    }

    @Test
    public void testFillTriangleWithNull() {
        int[][] triangle = null;
        Triangle3D.fillTriangle(triangle);
        assertNull(triangle);
    }
}
