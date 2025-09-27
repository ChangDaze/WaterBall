package StatePattern.v2;

public class TicketSystem {
    static final int PRICE = 3;
    private State state;
    private int totalCoins;
    private int tickets;
    private boolean lightOn = false;

    public TicketSystem(int ticketsInStock) {
        this.tickets = ticketsInStock;
        this.state = tickets == 0 ? new SoldOutState(this) : new InStockState(this);
    }

    void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public int getTickets() {
        return tickets;
    }

    public void insertCoin(){
        System.out.println("[ACTION] insert a coin.");
        state.insertCoin();
    }

    void updateCoinsDisplay(){
        System.out.printf("Total Coins: %d.\n", totalCoins);
    }

    void turnLight(boolean on) {
        this.lightOn = on;
        System.out.printf("The light is %s.\n", on ? "on" : "off");
    }

    void spitCoins(int coins) {
        totalCoins -= coins;
        System.out.printf("Spitting the coins: %d.\n", coins);
    }

    void fillTickets(int tickets) {
        System.out.printf("[ACTION] Fill in tickets : %d.\n", tickets);
        state.fillInTickets(tickets);
    }

    public void pressBuyButton() {
        System.out.println("[ACTION] Press the buy button.");
        state.pressRefundButton();
    }

    void issueOneTicket(){
        System.out.println("You have bought one ticket.");

        if (--tickets <= 0) {
            enterState(new SoldOutState(this));
        } else {
            enterState(new InStockState(this, tickets));
        }
    }

    public void pressRefundButton() {
        System.out.println("[ACTION] Press the refund button.");
        state.pressRefundButton();
    }

    void enterState(State state) {
        this.state.exitState();
        this.state = state;
        System.out.printf("State updated: %s.\n", state);
        this.state.enterState();
    }
}
