package StrategyPattern.v2;

public class Powerball implements AttackType{

    @Override
    public void attack(Hero attacker, Hero attacked){
        attacked.damage(500);
    }
}
