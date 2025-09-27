package Role;

public class Healing extends State{
    public Healing(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        System.out.printf("%s is Healing \n", role.getSymbol());
        role.setHp(Math.min(role.getHp() + 30, role.getHpLimit()));
        if(role.getHp() >= role.getHpLimit()) {
            System.out.printf("%s is full Healed \n", role.getSymbol());
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
        else if(role.getGameMap().getRound() - enterStateRound > 5) {
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
    }
}
