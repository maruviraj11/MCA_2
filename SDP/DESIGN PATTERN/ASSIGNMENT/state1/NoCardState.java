class NoCardState implements ATMState {

    public void insertCard(ATMContext atm) {
        System.out.println("Card Inserted");
        atm.setState(new HasCardState());
    }

    public void ejectCard(ATMContext atm) {
        System.out.println("No Card to Eject");
    }
}