package Strategy;

import Battle.Unit;
import Skill.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@SuperBuilder
@Getter
public abstract class RoleStrategy implements Strategy{
    private final Unit unit;

    @Override
    public void action() {

        //s1
        Skill skill;
        do {
            unit.printSkills();
            skill = chooseSkill();
        } while (!checkSkillMp(skill));

        //s2
        List<Unit> targets = skill.chooseTarget(unit);

        //s3
        execute(skill, targets);
    }

    private boolean checkSkillMp(Skill skill) {
        if (unit.getMp() >= skill.getNeedMp(unit)) {
            return true;
        }
        else {
            System.out.println("你缺乏 MP，不能進行此行動。");
            return false;
        }
    }

    public abstract Skill chooseSkill();

    @Override
    public abstract List<Unit> chooseTarget(List<Unit> potentialTargets, Integer requiredNumber);

    public void execute(Skill skill, List<Unit> targets) {
        printExecute(skill, targets);
        unit.setMp(unit.getMp() - skill.getNeedMp(unit));
        for (Unit target : targets) {
            skill.apply(unit, target);
        }
        //HP使用沒和使用MP放一起只是為了讓測試印出訊息順序一樣而已
        //可能MP使用也要擴充單獨的方法來做其他修飾
        unit.damageReceived(skill.getNeedHp(unit));
    }

    private void printExecute(Skill skill, List<Unit> targets){
        //感覺如果印出訊息要客製化這部分要把邏輯放到各個skill，不然邏輯一直變長

        if(skill instanceof BasicAttack) {
            System.out.printf("%s 攻擊 %s。\n"
                    ,unit.getNameWithTroop()
                    ,targets.stream().map(Unit::getNameWithTroop).collect(Collectors.joining(", ")));
        }else if (skill instanceof SelfHealing || skill instanceof Summon ) {
            System.out.printf("%s 使用了 %s。\n"
                    ,unit.getNameWithTroop()
                    ,skill.getSkillName());
        }
        else {
            System.out.printf("%s%s%s 使用了 %s。\n"
                    ,unit.getNameWithTroop()
                    , !targets.isEmpty() ? " 對 " : ""
                    ,targets.stream().map(Unit::getNameWithTroop).collect(Collectors.joining(", "))
                    ,skill.getSkillName());
        }


    }
}
