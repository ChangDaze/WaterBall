package Role;

import Map.GameMap;

public class Erupting extends State{
    public Erupting(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        if(role.getGameMap().getRound() - enterStateRound > 3) {
            role.setState(new Teleport(role, role.getGameMap().getRound()));
        }
    }

    @Override
    protected void attack(GameMap gameMap) {

        for (Role role : gameMap.getRoles()) {
            if (role.equals(this.role)){
                continue;
            }
            role.state.attacked(this.role);
        }
    }
}
