public class Main {
    public static void main(String[] args) {

        ATMContext atm = new ATMContext();

        atm.insertCard();  // Insert card
        atm.insertCard();  // Already inserted
        atm.ejectCard();   // Remove card
        atm.ejectCard();   // No card
    }
}