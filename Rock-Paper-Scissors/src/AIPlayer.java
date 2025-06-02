public class AIPlayer extends Player {

    public AIPlayer(int number) {
        super(number);
    }

    @Override
    public Decision decide() {
        int randomNum = (int) (Math.random() * 3);
        switch (randomNum) {
            case 0:
                return Decision.ROCK;
            case 1:
                return Decision.PAPER;
            default:
                return Decision.SCISSORS;
        }
    }
}
