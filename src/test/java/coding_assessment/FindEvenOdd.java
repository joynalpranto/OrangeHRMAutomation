package coding_assessment;

import java.util.Scanner;

public class FindEvenOdd {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //  User input
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();

        // Check even or odd
        if (number % 2 == 0) {
            System.out.println(number + " is Even.");
        } else {
            System.out.println(number + " is Odd.");
        }

        scanner.close();
    }
}
