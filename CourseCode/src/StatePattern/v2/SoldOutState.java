package StatePattern.v2;

public class SoldOutState extends State{

    public SoldOutState(TicketSystem ticketSystem){
        super(ticketSystem);
    }

    @Override
    public void insertCoin() {
        ticketSystem.setTotalCoins(ticketSystem.getTotalCoins() + 1);
        ticketSystem.spitCoins(ticketSystem.getTotalCoins());
    }

    @Override
    public void pressBuyButton() {
        System.out.println("[ERROR] You havent't inserted enough coins.");
    }

    @Override
    public void fillInTickets(int tickets) {
        ticketSystem.enterState(new InStockState(ticketSystem, ticketSystem.getTickets() + tickets));
    }
}
