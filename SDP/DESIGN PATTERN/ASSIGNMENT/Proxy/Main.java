import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Book Store");
        System.out.println("Available Books:");
        System.out.println("1. Java");
        System.out.println("2. Python");
        System.out.println("3. C++");

        System.out.print("Enter the Book name: ");
        String fileName = sc.nextLine();

        Book b = new BookProxy(fileName);
 
        System.out.println("\n" + b.display());

        sc.close();
    }
}