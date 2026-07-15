import java.util.*;
public class demomain {
    
    public static void main(String[] args)
    {

        Scanner sc = new Scanner(System.in);

        System.out.println("enter a book name");
        String filename=sc.nextLine();

        System.out.println("\n list of all books");
        System.out.println("\n1.java");
        System.out.println("\n2.c++");
        System.out.println("\n3.orecal");

        book b = new proxybook(filename);

       System.out.println("\n"+ b.display());

        sc.close();

    }
}
