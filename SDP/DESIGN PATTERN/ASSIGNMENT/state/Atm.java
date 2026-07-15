public class Atm {

    private AtmState state;

    public Atm() {
        state = new NoCardState();
    }

    public void setState(AtmState state) {
        this.state = state;
    }

    public void insertCard() {
        state.insertCard(this);
    }

    public void enterPin() {
        state.enterPin(this);
    }

    public void withdraw() {
        state.withdraw(this);
    }
}