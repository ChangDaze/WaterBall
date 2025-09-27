package Role;

import Map.GameMap;
import Map.MapObject;

import java.util.List;
import java.util.Random;

public class Orderless extends State{
    public Orderless(Role role, int enterStateRound) {
        super(role, enterStateRound);
    }

    @Override
    protected void roundStart() {
        if(role.getGameMap().getRound() - enterStateRound > 3) {
            role.setState(new Normal(role, role.getGameMap().getRound()));
        }
    }

    @Override
    protected void action() {
        System.out.printf("%s do Orderless action start \n", role.getSymbol());

        List<String> actions = List.of("move");
        int[][] directions1 = {
                {1, 0},
                {-1, 0}
        };

        int[][] directions2 = {
                {0, 1},
                {0, -1}
        };

        Random random = new Random();
        int[][] directions = random.nextBoolean() ? directions1 : directions2;

        role.doAtion(actions, directions);

        System.out.printf("%s do Orderless action end \n", role.getSymbol());
    }
}
