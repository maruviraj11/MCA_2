import java.util.Scanner;
public class facmain {
    public static void main(String[] args)
    {
        Scanner sc= new Scanner(System.in);

        System.out.println("1.window");
        System.out.println("2.mac");

        System.out.println("ENTER YOUR CHOICE..");
        int ch=sc.nextInt();

        abstractfactory factory=  abstractfactory.getfactory(ch);

        button b= factory.createbutton();
        checkbox c= factory.createCheckbox();

        b.click();
        c.click();
        sc.close();
    }
    
}
