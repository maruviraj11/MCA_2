import java.util.Scanner;

public class Client {
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        System.out.println("----Welcome Sundervan Snake Park----");

        System.out.println("Enter Snake Type (cobra/viper/ratsnake/python):");
        String type = sc.nextLine().toLowerCase();

        System.out.println("Enter Bite Type (poisonous/non-poisonous):");
        String biteType = sc.nextLine().toLowerCase();

        BiteStrategy strategy;
        if(biteType.equals("poisonous")){
            strategy = new PoisonousBite();
        } else {
            strategy = new NonPoisonousBite();
        }

        Snake snake = null;

        switch (type) {
            case "cobra":
                snake = new Cobra(strategy);
                break;

            case "viper":
                snake = new Viper(strategy);
                break;

            case "ratsnake":
                snake = new RatSnake(strategy);
                break;

            case "python":
                snake = new Python(strategy);
                break;

            default:
                System.out.println("Invalid Snake Type..");
                sc.close();
                return; 
        }

        snake.PerfomBite();
        snake.display();

       
        System.out.println("Do you want to Change bite Behavior? (yes/no)");
        String choice = sc.nextLine();

        if(choice.equalsIgnoreCase("yes")){
            System.out.println("Enter new Bite Type (poisonous/non-poisonous): ");
            String newType = sc.nextLine().toLowerCase();

            if(newType.equals("poisonous")){
                System.out.println("Snake bite behavior updated to Poisonous.");
                snake.setBiteStrategy(new PoisonousBite());
            } else {
                snake.setBiteStrategy(new NonPoisonousBite());
                System.out.println("Snake bite behavior updated to Non-Poisonous.");
            }

        }

        sc.close();
    }
}