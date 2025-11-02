package Battle;

import Link.Link;
import Skill.Skill;
import State.State;
import Strategy.Strategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Getter
@Setter
public class Unit {
    private final String name;
    private Integer hp;
    private Integer mp;
    private Integer str;
    private boolean isDead;
    private Strategy strategy;
    private State state;
    private List<Link> links;
    private List<Skill> skills;
    private final Troop troop;

    public String getStatus() {
        return String.format("%s (HP: %d, MP: %d, STR: %d, State: %s)",
                getNameWithTroop(), hp, mp, str, state.toString());
    }

    public Integer getRound() {
        return troop.getBattle().getRound();
    }

    public Battle getBattle() {
        return troop.getBattle();
    }

    /**
     * 有來去對象的damage ex:attack
     */
    public void damageReceived(Unit from, Unit to, Integer damage) {
        this.hp = hp - damage;
        System.out.printf("%s 對 %s 造成 %d 點傷害。\n", from.getNameWithTroop(), to.getNameWithTroop(), damage);
        deadCheck();
    }

    /**
     * 沒有來去對象的damage ex:PoisonedState
     */
    public void damageReceived(Integer damage) {
        this.hp = hp - damage;
        deadCheck();
    }

    private void deadCheck() {
        if ( this.hp <= 0) {
            this.isDead = true;
            System.out.printf("%s 死亡。\n", getNameWithTroop());

            for(Link link : links) {
                link.linkUpdate();
            }

            links = new ArrayList<>();
        }
    }

    public void followUpApply(Unit unit) {
        state.followUpApply(unit);
    }

    public void action() {
        if(state.roundStart()) {
            strategy.action();
        }
    }

    public void printSkills(){
        String skillsStr = IntStream.range(0, skills.size())
                .mapToObj(i -> String.format("(%d) %s", i, skills.get(i).getSkillName()))
                .collect(Collectors.joining(" "));
        System.out.printf("選擇行動：%s\n", skillsStr);
    }

    public String getNameWithTroop(){
        return String.format("%s%s",troop.getName(),name);
    }

    public Integer getFollowUpDamage() {
        return state.getFollowUpDamage();
    }
}
