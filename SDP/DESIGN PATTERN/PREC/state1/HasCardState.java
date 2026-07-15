class HasCardState implements ATMState {

    public void insertCard(ATMContext atm) {
        System.out.println("Card already inserted");
    }

    public void ejectCard(ATMContext atm) {
        System.out.println("Card Removed");
        atm.setState(new NoCardState());
    }
}