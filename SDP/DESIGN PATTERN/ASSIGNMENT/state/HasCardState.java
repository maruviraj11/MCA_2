public class HasCardState implements AtmState {

    public void insertCard(Atm atm) {
        System.out.println("Card already inserted");
    }

    public void enterPin(Atm atm) {
        System.out.println("Pin Verified");
        atm.setState(new PinVerificationState());
    }

    public void withdraw(Atm atm) {
        System.out.println("Enter pin first");
    }
}