package Skill;

import Battle.*;
import Link.*;

import java.util.List;
import java.util.stream.Collectors;

public class Curse implements Skill{
    @Override
    public List<Unit> chooseTarget(Unit from) {
        Battle battle = from.getBattle();
        Troop allyTroop = from.getTroop();

        List<Unit> potentialTargets = battle.getTroops().stream()
                .filter(t -> t != allyTroop)                 // 過濾掉自己所在的 troop
                .flatMap(t -> t.getUnits().stream().filter(u -> !u.isDead()))       // 攤平成所有 unit
                .collect(Collectors.toList());

        if(getTargetRequiredNumber() >= potentialTargets.size()){
            return potentialTargets;
        }

        return from.getStrategy().chooseTarget(potentialTargets, getTargetRequiredNumber());
    }

    @Override
    public void apply(Unit from, Unit to) {
        List<Link> links = to.getLinks();

        if(links.stream().anyMatch(l -> l.getLinkSource() == from)){
            return;
        }

        links.add(
            CurseLink.builder().linkSource(from).linkDestination(to).build()
        );
    }

    @Override
    public Integer getNeedHp(Unit from) {
        return 0;
    }

    @Override
    public Integer getNeedMp(Unit from) {
        return 100;
    }

    @Override
    public Integer getTargetRequiredNumber() {
        return 1;
    }

    @Override
    public String getSkillName(){
        return "詛咒";
    }
}
