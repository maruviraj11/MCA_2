public interface AtmState {
    void insertCard(Atm atm);
    void enterPin(Atm atm);
    void withdraw(Atm atm);
}