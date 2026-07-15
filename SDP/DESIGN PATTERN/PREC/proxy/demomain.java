import java.util.*;
public class demomain {

public static void main(String[] args)
{
    Scanner sc = new Scanner(System.in);

    System.out.println("\nChoice your book\n");
    System.out.println("\n1.java\n");
    System.out.println("\n2.python\n");
    System.out.println("\n 3. web \n");

    System.out.println("Enter your book name\n");
    String filename = sc.nextLine();

    book b = new proxybook(filename);

    System.out.println(b.display());

    sc.close();
        


}
    
}
