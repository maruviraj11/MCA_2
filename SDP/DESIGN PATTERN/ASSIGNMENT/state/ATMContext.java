class ATMContext {
    private ATMState state;

    ATMContext() {
        state = new NoCardState();
    }

    void setState(ATMState state) {
        this.state = state;
    }

    void insertCard() {
        state.insertCard(this);
    }

    void ejectCard() {
        state.ejectCard(this);
    }
}