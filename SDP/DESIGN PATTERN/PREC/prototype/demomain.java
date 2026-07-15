import java.util.*;
public class demomain {

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter a a student id");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter a student name");
        String name = sc.nextLine();

        System.out.println("Enter a student cource ");
        String corce =sc.nextLine();

        System.out.println("Enter a student department");
       String department= sc.nextLine();

        student s1 = new student(id , name, corce,department);

        student s2 = (student)s1.clone();

        System.out.println("\nEnter a clone student data");

        System.out.println("Enter a student id");
        s2.setid(sc.nextInt());
        sc.nextLine();

        System.out.println("Enter aa student name");
        s2.setname(sc.nextLine());

        s1.display();
        s2.display();

        System.out.println(s1);
        System.out.println(s2);

        sc.close();
    }
}