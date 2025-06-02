package StrategyPattern.v2;

public class Main {
    public static void main(String[] args) {
        Hero hero1 = new Hero("地v2", new Earth());
        Hero hero2 = new Hero("力量球v2", new Powerball());
        Game game = new Game(hero1, hero2);
        game.start();
    }
}
