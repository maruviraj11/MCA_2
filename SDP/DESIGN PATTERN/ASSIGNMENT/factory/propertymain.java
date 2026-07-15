import java.util.Scanner;

public class propertymain {

    public static void main(String[] args)
    {

        Scanner sc =new Scanner(System.in);

        System.out.println("ENTER CITY AHEMDABAD/RAJKOT/SURAT");
        String city =sc.nextLine();

        System.out.println("ENTER A PROPERTY VALUE");
        double value=sc.nextDouble();

        propertytax tax = propertytaxFactory.gettax(city);

        if (tax != null) {
            double totalTax = tax.caltax(value);
            System.out.println("Total Property Tax: ₹ " + totalTax);
        } else {
            System.out.println("Invalid City!");
            
        }

        sc.close();

    }
    
}
