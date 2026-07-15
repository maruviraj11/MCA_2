
import java.util.Scanner;

public class Main {
    public static void main(String [] args){
        Scanner sc=new Scanner(System.in);
        loanHandler Manager= manager.getInstance();
        loanHandler Supervisor=supervisor.getInstance();
        loanHandler Director=director.getInstance();
        Manager.setNext(Supervisor);
        Supervisor.setNext(Director);
        int choice;
        do{
            System.out.println("\n Loan Approval System");
            System.out.println("1. Apply for Loan");
            System.out.println("2.Exit");
            System.out.println("Enter Choice :");
            choice=sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter Loan Amount");
                    int amount= sc.nextInt();

                    if(amount > 10000000){
                        System.out.println("Amount Exceeds limit");

                    }
                    else{
                        System.out.println( "\n Processing Loan .........");
                        Manager.handleRequest(amount);
                    }
                    
                    
                    break;
                
                case 2: 
                    System.out.println("Exiting .........");
                    break;      
                default:
                    System.out.println("Invalid Choice");
                    break;
            }
        }while ( choice!=2); 
            
        sc.close();
        
    }
}
