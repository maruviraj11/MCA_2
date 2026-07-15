import java.util.Scanner;
public class facmain {
 
    
    public static void main(String[] args)
    {
        Scanner sc=new Scanner(System.in);

        System.out.println("ENTER YOUR CHOICE FOR FACTORY");
        int ch=sc.nextInt();

        abstractfactory factory=abstractfactory.createfactory(ch);

        button b= factory.createbutton();
        check c=factory.createcheckbox();

       System.out.println( b.click());
        System.out.println(c.click());
        sc.close();
    }
}
