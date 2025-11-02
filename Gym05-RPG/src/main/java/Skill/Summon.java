package Skill;

import Battle.*;
import Link.SummonLink;
import State.NormalState;
import State.PetrochemicalState;
import Strategy.AIStrategy;

import java.util.ArrayList;
import java.util.List;

public class Summon implements Skill{
    @Override
    public List<Unit> chooseTarget(Unit from) {
        return new ArrayList<>(List.of(from));
    }

    @Override
    public void apply(Unit from, Unit to) {
        Troop troop = from.getTroop();
        List<Unit> allies = troop.getUnits();


        Unit newAlly = Unit.builder()
            .name("Slime")
            .hp(100)
            .mp(0)
            .str(50)
            .isDead(false)
            .troop(troop)
            .links(new ArrayList<>())
            .skills(new ArrayList<>(List.of(new BasicAttack())))
            .build();

        newAlly.setState(
            NormalState.builder()
                .unit(newAlly)
                .startRound(newAlly.getRound())
                .build()
        );

        newAlly.setStrategy(
            AIStrategy.builder()
                .unit(newAlly)
                .seed(0)
                .build()
        );

        newAlly.getLinks().add(SummonLink.builder().linkSource(from).build());

        allies.add(newAlly);
    }

    @Override
    public Integer getNeedHp(Unit from) {
        return 0;
    }

    @Override
    public Integer getNeedMp(Unit from) {
        return 150;
    }

    @Override
    public Integer getTargetRequiredNumber() {
        return 0;
    }

    @Override
    public String getSkillName(){
        return "召喚";
    }
}
