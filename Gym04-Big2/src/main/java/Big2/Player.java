package Big2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Player {
    private final String name;
    private final Scanner scanner;

    public List<Card> getHandCards() {
        return handCards;
    }

    public String getName() {
        return name;
    }

    private final List<Card> handCards = new ArrayList<>();

    public Player(String name, Scanner scanner){
        this.name = name;
        this.scanner = scanner;
    }

    public void Draw(Deck deck) {
        handCards.add(deck.dealOneCard());
    }

    public CardPattern Play(CardPatternInterpreter interpreter){
        printHandCardsWithIndex();

        String command = scanner.nextLine();

        List<Integer> indexs = Arrays.stream(command.trim().split("\\s+"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Card> combine = new ArrayList<>();

        if (indexs.getFirst() != -1){
            for (var i : indexs){
                combine.add(handCards.get(i));
            }
        }

        return interpreter.interprete(combine);
    }

    private void printHandCardsWithIndex() {
        // 先將所有文字準備好
        List<String> indexes = new ArrayList<>();
        List<String> cardStrings = new ArrayList<>();

        int n = handCards.size();
        for (int i = 0; i < n; i++) {
            indexes.add(String.valueOf(i));
            cardStrings.add(handCards.get(i).toString());
        }

        // 找出每個欄位對應的寬度（取 index 和 card 的最大長度）+1
        int[] colWidths = new int[n];
        for (int i = 0; i < n; i++) {
            int maxLen = Math.max(indexes.get(i).length(), cardStrings.get(i).length());
            colWidths[i] = maxLen + 1;
        }

        // 印索引
        for (int i = 0; i < n; i++) {
            if (i == n-1){
                System.out.print(indexes.get(i));
            }else {
                System.out.print(padRight(indexes.get(i), colWidths[i]));
            }
        }
        System.out.println();

        // 印牌
        for (int i = 0; i < n; i++) {
            if (i == n-1){
                System.out.print(cardStrings.get(i));
            }else {
                System.out.print(padRight(cardStrings.get(i), colWidths[i]));
            }
        }
        System.out.println();

    }

    private String padRight(String text, int length) {
        if (text.length() >= length) {
            return text; // 不補空白
        }
        return text + " ".repeat(length - text.length());
    }
}
