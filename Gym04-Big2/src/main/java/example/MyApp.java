package example;

import java.util.Scanner;

public class MyApp {
    public static void run() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // 假設邏輯是把每行加上 "Out: "
            System.out.println("Out: " + line);
        }
    }

    public static void main(String[] args) {
        run();
    }
}
