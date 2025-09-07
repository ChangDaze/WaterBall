package Big2;

import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Big2 {
    private final CardParser cardParser = new CardParser();
    private Deck deck;
    private Player[] players = new Player[4];
    private final Scanner scanner = new Scanner(System.in);
    private int topPlayerIndex;
    private CardPattern topPlay;
    private final CardPatternInterpreter interpreter = new FullhouseInterpreter(new StraightInterpreter(new PairInterpreter(new SingleInterpreter(new PassInterpreter(null)))));
    private Player winner;
    private int passTime = 0;
    private int round = 0;
    private int roundPlay = 0;

    public void start(){
        winner = null;
        createDeck();
        createPlayers();
        dealCards();
        setFirstPlayer();
        System.out.println("新的回合開始了。");
        round = 1;
        while (winner == null){
            runRound();
        }
        System.out.printf("遊戲結束，遊戲的勝利者為 %s%n", winner.getName());
    }

    private void createDeck(){
        String cardsString = scanner.nextLine();
        Card[] cards = cardParser.parseCards(cardsString);
        deck = new Deck(cards);
    }

    private void createPlayers(){
        for (int i =0; i< players.length; i++){
            String name = scanner.nextLine();
            players[i] = new Player(name, scanner);
        }
    }

    private void dealCards(){
        while (deck.remainingCards() > 0) {
            for(Player player : players){
                player.Draw(deck);
            }
        }

        for(Player player : players){
            Collections.sort(player.getHandCards());
        }
    }

    /**
     * 尋找持有梅花3的Player
     */
    private void setFirstPlayer(){
        for(int i = 0; i < players.length; i++){
            boolean hasClub3 = false;
            var hands = players[i].getHandCards();
            for (var hand : hands) {
                if (hand.sameCard(Suit.CLUBS, Rank.THREE)){
                    topPlayerIndex = i;
                    hasClub3 = true;
                    break;
                }
            }

            if (hasClub3){
                break;
            }
        }
    }

    private boolean compareTop(CardPattern cardPattern){

        boolean hasTopPlay = topPlay != null;

        if(cardPattern == null){
            System.out.println("此牌型不合法，請再嘗試一次。");
            return false;
        }

        boolean pass = cardPattern.getType() == CardPatternType.PASS;
        if(!hasTopPlay && pass){
            System.out.println("你不能在新的回合中喊 PASS");
        }
        else if (round == 1 && roundPlay == 0 &&
                cardPattern.getCombine().stream().noneMatch(card -> card.sameCard(Suit.CLUBS, Rank.THREE))){
            System.out.println("首次出牌請出含梅花3的牌型");
        }else if(hasTopPlay && !pass &&
                (topPlay.getType() != cardPattern.getType() ||
                topPlay.getBiggestCard().compareTo(cardPattern.getBiggestCard()) > 0)){
            System.out.println("此牌型不合法，請再嘗試一次。");
        }else {
            if(!pass){
                topPlay = cardPattern;
            }
            return true;
        }

        return  false;
    }

    private void runRound(){
        int n = players.length;
        for (int i = 0; i < n; i++) {
            int currentIndex = (topPlayerIndex + i) % n;
            CardPattern cardPattern = null;
            var player = players[currentIndex];
            System.out.printf("輪到%s了%n", player.getName());

            do
            {
                cardPattern = player.Play(interpreter);
            }
            while (!compareTop(cardPattern));

            player.getHandCards().removeAll(cardPattern.getCombine());

            playEnd(player, cardPattern);
            if (checkWinner(player)){
                break;
            }
        }
    }

    private boolean checkWinner(Player player){
        if (player.getHandCards().isEmpty()){
            winner = player;
            return true;
        }

        return false;
    }

    private void playEnd(Player player, CardPattern cardPattern){
        var type = cardPattern.getType();

        if(type == CardPatternType.PASS){
            System.out.printf("玩家 %s %s.%n", player.getName(), type.getDisplayName());
            passTime ++;
        }else {
            System.out.printf("玩家 %s 打出了 %s %s%n", player.getName(), type.getDisplayName(), cardPattern.getCombine().stream()
                    .map(Card::toString) // 呼叫每張牌的 toString()
                    .collect(Collectors.joining(" ")));
            passTime = 0;
        }

        roundPlay++;

        if (passTime == 3){
            passTime=0;
            round++;
            topPlay=null;
            roundPlay = 0;
            System.out.println("新的回合開始了。");
        }
    }
}
