package StrategyPattern.v1;

public class Main {
    public static void main(String[] args) {
        Hero hero1 = new Hero("水之球", "Waterball");
        Hero hero2 = new Hero("火之球", "Fireball");
        Game game = new Game(hero1, hero2);
        game.start();
    }
}
