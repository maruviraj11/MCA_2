import java.util.Scanner;

public class vihmain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("ENTER A VEHICLE NAME:");
        String vihcal = sc.nextLine();

        vihcal v1 = vihcalfactory.gettype(vihcal);

        if (v1 != null) {
           //System.out.println( v1.getvih(vihcal));
           String s1=v1.getvih(vihcal);
           System.out.println(s1);
        } else {
            System.out.println("Invalid Vehicle Type");
        }

        sc.close();
    }
}