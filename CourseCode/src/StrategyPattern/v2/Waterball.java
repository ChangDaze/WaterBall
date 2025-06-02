package StrategyPattern.v2;

public class Waterball implements AttackType{

    @Override
    public void attack(Hero attacker, Hero attacked){
        attacked.damage((int)(attacker.getHp()*0.5));
    }
}
