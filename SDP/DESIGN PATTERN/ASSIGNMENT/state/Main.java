import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Atm atm = new Atm();

        int choice;

        do {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Insert Card");
            System.out.println("2. Enter Pin");
            System.out.println("3. Withdraw Cash");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    atm.insertCard();
                    break;

                case 2:
                    atm.enterPin();
                    break;

                case 3:
                    atm.withdraw();
                    break;

                case 4:
                    System.out.println("Thank you...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 4);

        sc.close();
    }
}