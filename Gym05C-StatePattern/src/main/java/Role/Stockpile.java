package Role;

public class Stockpile extends State{
    public Stockpile(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        if(role.getGameMap().getRound() - enterStateRound > 2) {
            role.setState(new Erupting(role, role.getGameMap().getRound()));
        }
    }

    @Override
    protected void attacked(Role from) {
        System.out.printf("%s is Stockpile but be attacked \n", role.getSymbol());
        role.setState(new Normal(role, role.getGameMap().getRound()));
    }
}
