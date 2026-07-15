package client;

import db.DatabaseConnection;
import db.DatabaseFactory;

import java.util.Scanner;

public class DatabaseGUI {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            System.out.println("Enter Database Type (mysql/postgresql/oracle): ");
            String type = sc.nextLine();

           
            DatabaseConnection db = DatabaseFactory.getDatabase(type);

            if (db == null) {
                System.out.println("Invalid database type");
                return;
            }

            System.out.println("Enter Driver: ");
            String driver = sc.nextLine();

            System.out.println("Enter URL: ");
            String url = sc.nextLine();

            System.out.println("Enter Username: ");
            String username = sc.nextLine();

            System.out.println("Enter Password: ");
            String password = sc.nextLine();

            db.connect(driver, url, username, password);
            System.out.println("Connected Successfully");

            while (true) {
                System.out.println("\nEnter SQL Query: ");
                String query = sc.nextLine();

                try {
                    int result = db.executeQuery(query);

                    if (!query.trim().toLowerCase().startsWith("select")) {
                        System.out.println("Rows Affected: " + result);
                    }
                } catch (Exception e) {
                    System.out.println("Query Error: " + e.getMessage());
                }
                System.out.println("\nDo you want to execute another query? (yes/no): ");
                String choice = sc.nextLine();
                if (!choice.equalsIgnoreCase("yes")) {
                    break;
                }
            }

            db.disconnect();
            System.out.println("Disconnected Successfully");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}
