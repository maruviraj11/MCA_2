public class PinVerificationState implements AtmState {

    public void insertCard(Atm atm) {
        System.out.println("Card already inserted");
    }

    public void enterPin(Atm atm) {
        System.out.println("Pin already verified");
    }

    public void withdraw(Atm atm) {
        System.out.println("Cash Withdrawn");
        atm.setState(new NoCardState());
    }
}