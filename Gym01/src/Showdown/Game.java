package Showdown;

import java.util.List;

public class Game {

    private final List<Player> players;

    public Game(List<Player> players) {
        this.players = players;
    }

    public void Start() {
        //命名
        for (Player player : players) {
            player.nameHimself();
            System.out.printf("玩家 #%s 出現了\n", player.getUniqueID());
        }

        //洗牌
        Deck deck = new Deck();

        //抽牌
        for (int i = 0; i < 13; i++) {
            for (Player player : players) {
                Card card = deck.drawCard();
                player.takeCard(card);
            }
        }

        //比大小
        for (int i = 0; i < 13; i++) {

            //比大小邏輯
            Player biggestPlayer = null;
            Card biggestCard = null;

            for (Player player : players) {
                //每次都詢問是否換牌
                player.exchangeHands(players, i+1); //回合從1開始

                Card card = player.show();
                //show 出 出牌
                System.out.printf("玩家 #%s 出了 rank : %d, suit : %d\n", player.getUniqueID(), card.getRank(), card.getSuit());

                if(
                    biggestPlayer == null ||
                    card.getRank() > biggestCard.getRank() ||
                    (card.getRank() == biggestCard.getRank() && card.getSuit() > biggestCard.getSuit())
                ) {
                    biggestPlayer = player;
                    biggestCard = card;
                }
            }

            assert biggestPlayer != null;
            biggestPlayer.addScore();
        }

        //印出勝利者
        Player winner = null;

        for (Player player : players) {
            if(
                winner == null ||
                player.getScore() > winner.getScore()
            )
            {
                winner = player;
            }
        }

        assert winner != null;
        System.out.printf("winner : %s\n", winner.getName());
    }
}
