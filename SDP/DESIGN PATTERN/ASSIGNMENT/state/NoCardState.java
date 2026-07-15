public class NoCardState implements AtmState {

    public void insertCard(Atm atm) {
        System.out.println("Card Inserted");
        atm.setState(new HasCardState());
    }

    public void enterPin(Atm atm) {
        System.out.println("Insert card first");
    }

    public void withdraw(Atm atm) {
        System.out.println("Insert card first");
    }
}