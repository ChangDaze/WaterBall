package StatePattern.v2;

public abstract class State {
    protected TicketSystem ticketSystem;

    public State(TicketSystem ticketSystem) {
        this.ticketSystem = ticketSystem;
    }

    public abstract void insertCoin();
    public abstract void pressBuyButton();

    public void enterState() { /*hook*/ }
    public void exitState() { /*hook*/ }

    public void fillInTickets(int tickets) {
        ticketSystem.setTickets(ticketSystem.getTickets() + tickets);
    }

    public void pressRefundButton() {
        ticketSystem.spitCoins(ticketSystem.getTotalCoins());
    }
}
