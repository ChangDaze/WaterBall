package InputManager;

import lombok.Getter;

import java.util.Scanner;

public class InputManager {
    // 單例模式（全域共用一個）
    @Getter
    private static final InputManager instance = new InputManager();

    private Scanner scanner;

    private InputManager() {
        this.scanner = new Scanner(System.in);
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public String nextLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public int nextInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("請輸入整數：");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public int nextInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("請輸入整數：");
            scanner.next();
        }
        return scanner.nextInt();
    }
}

