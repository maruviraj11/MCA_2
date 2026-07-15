import java.util.Scanner;
public class shapedemo {
    public static void main(String[] args)
    {
        Scanner sc= new Scanner(System.in);
        System.out.println("ENTER A ANY CHOICE\n1.CIRLCLE \n 2.RECTANGLE \n 3.SQUARE \n 4.EXIT");
        int ch = sc.nextInt();

        switch(ch)
        {
            case 1:
                    circle c1 = new circle(5);
                    System.out.println("AREA OF CIRCLE.."+c1.area());
                    System.out.println("CIRCLE DREAW.."+c1.draw());
                    System.out.println("CIRCLE INFO.."+c1.info());
                    System.out.println("CIRCLE COLOR.."+c1.fillcolor());
                    break;
            case 2:
                    rectangle r1 = new rectangle(10,5);
                    System.out.println("AREA OF RECTANGLE.."+r1.area());
                    System.out.println("RECTANGLE DREAW.."+r1.draw());
                    System.out.println("RECTANGLE INFO.."+r1.info());
                    System.out.println("RECTANGLE COLOR.."+r1.fillcolor());
                    break;
            case 3:
                    square s1 = new square (10);
                    System.out.println("AREA OF SQUARE.."+s1.area());
                    System.out.println(" SQUARE DREAW.."+s1.draw());
                    System.out.println(" SQUARE INFO.."+s1.info());
                    System.out.println(" SQUARERECTANGLE COLOR.."+s1.fillcolor());
                    break;
            case 4:
                    System.exit(0);
                    break;
            default:
                    System.out.println("INVALID CHOICE..");
                    

        }

    }
    
}
