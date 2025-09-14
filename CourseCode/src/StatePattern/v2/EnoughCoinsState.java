package StatePattern.v2;

import static StatePattern.v2.TicketSystem.PRICE;

public class EnoughCoinsState extends State{
    public EnoughCoinsState(TicketSystem ticketSystem) {
        super(ticketSystem);
    }

    @Override
    public void enterState() {
        if (ticketSystem.getTickets() <= 0) {
            throw new IllegalStateException("Can not enter EnoughCoinsState if tickets <= 0");
        }
        ticketSystem.setTotalCoins(PRICE);
        ticketSystem.turnLight(true);
    }

    @Override
    public void exitState() {
        ticketSystem.turnLight(false);
    }

    @Override
    public void insertCoin() {
        System.out.println("[ERROR] You can't insert coins after the number of coins is enough.");
    }

    @Override
    public void pressBuyButton() {
        ticketSystem.setTotalCoins(ticketSystem.getTotalCoins() - PRICE);
        ticketSystem.updateCoinsDisplay();
        ticketSystem.issueOneTicket();
    }

    @Override
    public void pressRefundButton() {
        ticketSystem.spitCoins(ticketSystem.getTotalCoins());
        ticketSystem.enterState(new InStockState(ticketSystem));
    }


}
