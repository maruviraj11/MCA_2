
import java.util.Scanner;
public class demomain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

       
        System.out.println("enter student details");

        System.out.print("enter id: ");
        int id = sc.nextInt();
        sc.nextLine();   

        System.out.println("enter name: ");
        String name = sc.nextLine();

        System.out.print("enter cource: ");
        String course = sc.nextLine();
        sc.nextLine();

        System.out.print("enter department: ");
        String department = sc.nextLine();

        student s1 = new  student(id, name, course, department);

       
         student s2 = (student) s1.clone();

       
        System.out.println("\nenter clone student id and name");

        System.out.print("enter a new id: ");
        s2.setid(sc.nextInt());
        sc.nextLine();

        System.out.print("enter a new name: ");
        s2.setname(sc.nextLine());

   
        System.out.println("\nOriginal Student:");
        s1.display();

        System.out.println("\nCloned Student:");
        s2.display();

        System.out.println(s1);
        System.out.println(s2);
        sc.close();
    }
}