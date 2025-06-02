import java.util.Scanner;

public class HumanPlayer extends Player {
    private final static Scanner in = new Scanner(System.in);
    public HumanPlayer(int number) {
        super(number);
    }

    @Override
    public Decision decide() {
        System.out.println("請出 石頭 (0) 布 (1) 剪刀 (2) :");
        int num = in.nextInt();
        switch (num) {
            case 0:
                return Decision.ROCK;
            case 1:
                return Decision.PAPER;
            case 2:
                return Decision.SCISSORS;
            default:
                System.out.println("只能輸入範圍 0~2 的數字，請再輸入一次。");
                return this.decide();
        }
    }
}
