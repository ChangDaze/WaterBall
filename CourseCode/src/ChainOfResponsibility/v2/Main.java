package ChainOfResponsibility.v2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bot bot = new Bot(new HelpHandler(new AnswerMeHandler(new ExitHandler(new TailHandler(null)))));
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        System.out.println("Hello World! V2");
        while (!exit) {
            String input = scanner.nextLine();
            exit = bot.handle(input);
        }

        scanner.close();
    }
}
