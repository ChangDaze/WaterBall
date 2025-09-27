package Role;

public class Poisoned extends State{
    public Poisoned(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        System.out.printf("%s be poisoned \n", role.getSymbol());
        role.setHp(role.getHp() - 15);
        if(role.getHp() < 0) {
            System.out.printf("%s do disappear \n", role.getSymbol());
            role.disappear();
        }
        else if(role.getGameMap().getRound() - enterStateRound > 3) {
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
    }
}
