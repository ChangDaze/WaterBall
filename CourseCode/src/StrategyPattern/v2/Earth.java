package StrategyPattern.v2;

public class Earth implements AttackType{

    @Override
    public void attack(Hero attacker, Hero attacked){
        for( int i = 0; i < 10; i++){
            attacked.damage(20);
        }
    }
}
