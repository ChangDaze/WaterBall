package Role;

public class Accelerated extends State{
    public Accelerated(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        if(role.getGameMap().getRound() - enterStateRound > 3) {
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
    }

    @Override
    protected void attacked(Role from) {
        System.out.printf("%s is Accelerated but attacked \n", role.getSymbol());
        super.attacked(from);

        if(role.getHp() > 0) {
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
    }

    @Override
    protected void action() {
        super.action();
        System.out.printf("%s is Accelerated \n", role.getSymbol());
        super.action();
    }
}
