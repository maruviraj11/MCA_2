import java.util.Scanner;

public class facmain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("1. Windows");
        System.out.println("2. Mac");
        System.out.print("Enter Choice: ");
        int ch = sc.nextInt();

        abstractfactory factory = abstractfactory.createfactory(ch);

        button b = factory.createbutton();
        check c = factory.createcheckbox();

        b.click();
        c.click();

        sc.close();
    }
}
