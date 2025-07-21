public class MaximumSubarray {
    public static int maxSubArraySum(int[] arr) {
        int maxSoFar = arr[0];
        int currentMax = arr[0];

        for (int i = 1; i < arr.length; i++) {
            currentMax = Math.max(arr[i], currentMax + arr[i]);
            maxSoFar = Math.max(maxSoFar, currentMax);
        }

        return maxSoFar;
    }

    public static void main(String[] args) {
        int[] arr = {-2, 3, -4, -1, 2, 1, -5, 3};
        System.out.println(maxSubArraySum(arr)); // Output: 7
    }
}