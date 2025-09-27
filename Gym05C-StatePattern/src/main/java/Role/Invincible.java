package Role;

public class Invincible extends State{
    public Invincible(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        if(role.getGameMap().getRound() - enterStateRound > 2) {
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
    }

    @Override
    protected void attacked(Role from) {
        System.out.printf("%s is invincible nothing happen \n", role.getSymbol());
    }
}
