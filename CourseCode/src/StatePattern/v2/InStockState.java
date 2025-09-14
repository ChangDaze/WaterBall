package StatePattern.v2;

import static StatePattern.v2.TicketSystem.PRICE;

public class InStockState extends State{
    public InStockState(TicketSystem ticketSystem, int tickets) {
        this(ticketSystem);
        ticketSystem.setTickets(tickets);
    }

    public InStockState(TicketSystem ticketSystem) {
        super(ticketSystem);
    }

    @Override
    public void enterState() {
        if (ticketSystem.getTotalCoins() > PRICE) {
            //轉入 InStockState -> 將多餘的硬幣吐出去
            ticketSystem.spitCoins(PRICE - ticketSystem.getTotalCoins() - 1);
        }
        if (ticketSystem.getTickets() <= 0) {
            throw new IllegalStateException("Can not enter InStockState or EnoughState if tickets 0");
        }
    }

    @Override
    public void insertCoin() {
        ticketSystem.setTotalCoins(ticketSystem.getTotalCoins() + 1 );
        ticketSystem.updateCoinsDisplay();
        if( PRICE == ticketSystem.getTotalCoins()) {
            ticketSystem.enterState(new EnoughCoinsState(ticketSystem));
        }
    }

    @Override
    public void pressBuyButton() {
        System.out.println("[ERROR] You haven't inserted enough coins.");
    }
}
