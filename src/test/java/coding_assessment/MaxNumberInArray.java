package coding_assessment;

import java.util.Scanner;

public class MaxNumberInArray {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Take array size input
        System.out.print("Enter the number of elements in the array: ");
        int size = scanner.nextInt();

        // Declare and fill the array
        int[] numbers = new int[size];
        System.out.println("Enter " + size + " numbers:");

        for (int i = 0; i < size; i++) {
            numbers[i] = scanner.nextInt();
        }

        // Find the maximum number
        int max = findMax(numbers);
        System.out.println("Maximum number in the array is: " + max);

        scanner.close();
    }

    // Method to find maximum value in an array
    public static int findMax(int[] array) {
        int max = array[0];
        for (int num : array) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }
}
