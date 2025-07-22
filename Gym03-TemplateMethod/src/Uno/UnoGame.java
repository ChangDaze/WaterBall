package Uno;

import CardGame.CardGame;
import Showdown.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class UnoGame  extends CardGame<UnoCard, UnoDeck, UnoPlayer> {
    private final List<UnoPlayer> players = new ArrayList<>();
    private UnoDeck deck;
    private UnoDeck abandonedDeck;

    // <editor-fold desc="getter & setter">
    @Override
    public UnoDeck getDeck() {
        return deck;
    }

    @Override
    public UnoDeck getAbandonedDeck() {
        return abandonedDeck;
    }

    @Override
    public List<UnoPlayer> getPlayers() {
        return players;
    }
    // </editor-fold>

    @Override
    protected void initialize() {
        setPlayerLimits(4 - getHumanPlayerLimits());
        setHandDefault(5);
    }

    @Override
    protected void addNewPlayer(List<UnoPlayer> players) {
        UnoPlayer player = new UnoPlayer();
        player.nameHimself();
        players.add(player);
    }

    @Override
    protected void addNewHumanPlayer(List<UnoPlayer> players) {
        UnoPlayer player = new UnoHumanPlayer();
        player.nameHimself();
        players.add(player);
    }

    @Override
    protected void dealDeck() {
        this.deck = new UnoDeck();
        for (Color color : Color.values()) {
            for (int i = 0; i <= 9; i++) {
                this.deck.addCard(new UnoCard(color, i));
            }
        }
        this.deck.shuffle();

        this.abandonedDeck = new UnoDeck();
    }

    @Override
    protected void dealHand(UnoPlayer player, UnoDeck deck) {
        player.addHand(deck.drawCard());
    }

    @Override
    protected void takeTurn(UnoPlayer player, UnoDeck deck, UnoDeck abandonedDeck, List<Pair<UnoCard, UnoPlayer>> turnCards) {
        var showCard = player.show(abandonedDeck.getCards().isEmpty() ? null : abandonedDeck.getCards().getLast());

        if (showCard != null) {
            turnCards.add(Pair.of(showCard, player));
            abandonedDeck.addCard(showCard);
        }
        else {
            //沒出牌要抽牌
            if (deck.getCards().isEmpty()) {
                ResetDeck(deck, abandonedDeck);
            }
            player.addHand(deck.drawCard());
        }
    }

    /**
     牌用完就整理棄牌堆的牌重新使用
     */
    private void ResetDeck(UnoDeck deck, UnoDeck abandonedDeck) {
        deck.setCards(abandonedDeck.getCards());
        deck.shuffle();
        abandonedDeck.setCards(new ArrayList<>());
    }

    @Override
    protected boolean gameOver(int turn) {
        return players.stream().anyMatch(player -> player.getHands().isEmpty());
    }

    @Override
    public void checkWinner() {
        UnoPlayer winner  = players.stream().filter(p ->p.getHands().isEmpty()).findFirst().orElse(null);

        assert winner != null;
        setGameWinner(winner);
        System.out.printf("%s is winner\r\n", winner.getName());
    }
}
