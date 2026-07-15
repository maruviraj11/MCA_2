import java.util.Scanner;
public class promain {

    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("ENTER A ANY CITY");
        String city=sc.nextLine();

        System.out.println("ENTER A ANY PROVAL");
        double proval=sc.nextDouble();

        propertytax tax=propertytaxfactory.getcity(city);

        if(tax!=null)
        {
            double tottax=tax.caltax(proval);
            System.out.println("TOTAL TAX"+tottax);
        }
        else
        {
            System.out.println("INVALIED CITY");
        }
        sc.close();
    }
}
