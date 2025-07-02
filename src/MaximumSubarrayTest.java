
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MaximumSubarrayTest {

    @Test
    public void testMaxSubArraySumPositive() {
        int[] arr = {1, 2, 3, 4, 5};
        assertEquals(15, MaximumSubarray.maxSubArraySum(arr));
    }

    @Test
    public void testMaxSubArraySumNegative() {
        int[] arr = {-1, -2, -3, -4, -5};
        assertEquals(-1, MaximumSubarray.maxSubArraySum(arr));
    }

    @Test
    public void testMaxSubArraySumMixed() {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        assertEquals(6, MaximumSubarray.maxSubArraySum(arr));
    }

    @Test
    public void testMaxSubArraySumSingleElement() {
        int[] arr = {5};
        assertEquals(5, MaximumSubarray.maxSubArraySum(arr));
    }

    @Test
    public void testMaxSubArraySumFromMain() {
        int[] arr = {-2, 3, -4, -1, 2, 1, -5, 3};
        assertEquals(3, MaximumSubarray.maxSubArraySum(arr));
    }
}
