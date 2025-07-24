package ChainOfResponsibility.v3;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bot bot = new Bot(new HelpHandler(new AnswerMeHandler(new ExitHandler(null))));
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        System.out.println("Hello World! V3");
        while (!exit) {
            String input = scanner.nextLine();
            exit = bot.handle(input);
        }

        scanner.close();
    }
}
