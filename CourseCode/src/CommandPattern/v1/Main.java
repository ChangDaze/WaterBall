package CommandPattern.v1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AirConditioner ac = new AirConditioner();
        Fan fan = new Fan();
        Television tv = new Television();
        Controller controller = new Controller(ac, fan, tv);

        Scanner in = new Scanner(System.in);
        while (true){
            System.out.println("Click a button (0-5): ");
            int button = in.nextInt();
            controller.press(button);
        }
    }
}
