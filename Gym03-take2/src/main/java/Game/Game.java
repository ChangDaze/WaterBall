package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Game {
    protected int turn;
    protected Deck deck;
    protected List<Player> players;
    protected Map<Player, Card> playerShows = new HashMap<>();
    protected List<Card> shows = new ArrayList<>();

    public void start(){
        this.turn = 0;
        prepareDeck();
        beforePlay();
        play();
    }

    protected abstract void prepareDeck();

    protected abstract void beforePlay();

    protected void play(){
        while (!gameOver()){
            this.turn+=1;

            for(Player player : players){
                Card show = player.show(shows);
                afterShow(player, show);
                playerShows.put(player, show);

                if(show != null){
                    shows.add(show);
                }

                if(gameOver()){
                    break;
                }
            }

            compare(playerShows);
            turnOver();
        }
    }

    protected abstract boolean gameOver();

    protected void afterShow(Player player, Card show){
        //預設不做事 ex:poker
    }

    protected void compare(Map<Player, Card> playerShows){
        //預設不做事 ex:uno
    }

    protected void turnOver(){
        //預設不做事 ex:uno
    }


}
