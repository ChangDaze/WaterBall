package CommandPattern.v2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AirConditioner ac = new AirConditioner();
        Fan fan = new Fan();
        Television tv = new Television();
        Controller controller = new Controller();
        controller.setCommands(0, new FanNextLevelCommand(fan));
        controller.setCommands(1, new FanPreviousLevelCommand(fan));
        controller.setCommands(2, new TurnOnAirConditionerCommand(ac));
        controller.setCommands(3, new TurnOffAirConditionerCommand(ac));
        controller.setCommands(4, new TurnOnTvCommand(tv));
        controller.setCommands(5, new TurnOffTvCommand(tv));

        Scanner in = new Scanner(System.in);
        while (true){
            System.out.println("Click a button (0-5) or undo (-1) or redo (-2) : ");
            int button = in.nextInt();

            if(-1 == button){
                controller.undo();
            } else if (-2 == button) {
                controller.redo();
            } else {
                controller.press(button);
            }
        }
    }
}
