package Battle;

import InputManager.InputManager;
import Skill.*;
import Skill.OnePunch.OnePunchFactory;
import State.NormalState;
import Strategy.AIStrategy;
import Strategy.HeroStrategy;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Builder
@Getter
public class Battle {
    private List<Troop> troops;
    private Integer round;
    //感覺可以丟到skill或設定檔裡
    private static final Map<String, Supplier<Skill>> skillMap = Map.ofEntries(
            Map.entry("普通攻擊", BasicAttack::new),
            Map.entry("水球", Waterball::new),
            Map.entry("火球", Fireball::new),
            Map.entry("自我治療", SelfHealing::new),
            Map.entry("石化", Petrochemical::new),
            Map.entry("下毒", Posion::new),
            Map.entry("召喚", Summon::new),
            Map.entry("自爆", SelfExplosion::new),
            Map.entry("鼓舞", Cheerup::new),
            Map.entry("詛咒", Curse::new),
            Map.entry("一拳攻擊", OnePunchFactory::create)
    );

    public void createBattle() {
        InputManager input = InputManager.getInstance();

        Troop troop1 = createTroop("[1]", "#軍隊-1-開始", "#軍隊-1-結束", input);
        Troop troop2 = createTroop("[2]", "#軍隊-2-開始", "#軍隊-2-結束", input);

        this.getTroops().add(troop1);
        this.getTroops().add(troop2);
    }

    private Troop createTroop(String name, String startTag, String endTag, InputManager input) {
        String line = input.nextLine();
        if (!line.equals(startTag)) {
            throw new IllegalStateException("預期標記 " + startTag + "，但收到：" + line);
        }

        Troop troop = Troop.builder()
                .name(name)
                .units(new ArrayList<>())
                .battle(this)
                .build();

        while (true) {
            line = input.nextLine();
            if (line.equals(endTag)) break;

            String[] settings = line.split(" ");
            Unit newUnit = Unit.builder()
                    .name(settings[0])
                    .hp(Integer.parseInt(settings[1]))
                    .mp(Integer.parseInt(settings[2]))
                    .str(Integer.parseInt(settings[3]))
                    .isDead(false)
                    .troop(troop)
                    .links(new ArrayList<>())
                    .skills(new ArrayList<>())
                    .build();

            newUnit.setState(
                    NormalState.builder()
                            .unit(newUnit)
                            .startRound(newUnit.getRound())
                            .build()
            );

            List<Skill> skills = new ArrayList<>(List.of(new BasicAttack()));
            for (int i = 4; i < settings.length; i++) {
                String skillName = settings[i];
                Supplier<Skill> supplier = skillMap.get(skillName);

                if (supplier != null) {
                    skills.add(supplier.get());
                } else {
                    System.out.printf("⚠ 未知技能：%s%n", skillName);
                }
            }
            newUnit.setSkills(skills);

            // 設定策略
            if (newUnit.getName().equals("英雄")) {
                newUnit.setStrategy(HeroStrategy.builder()
                        .unit(newUnit)
                        .build());
            } else {
                newUnit.setStrategy(AIStrategy.builder()
                        .unit(newUnit)
                        .seed(0)
                        .build());
            }

            troop.getUnits().add(newUnit);
        }

        return troop;
    }

    public void startBattle() {
        outer:
        while (true){
            round++;

            // Step 1: 全部 unit 做 roundStart check
            for (Troop troop : troops) {
                for (Unit unit : troop.getUnits()) {
                    unit.getState().checkStateLimit();
                }
            }

            // Step 2: 每個 unit 執行行動
            //簡單好處 iterator好處是刪除時不用擔心索引位移，索引好處是新增資料比較安全
            for (Troop troop : troops) {
                List<Unit> units = troop.getUnits();
                for (int i = 0; i < units.size(); i++) { //因為有summon所以不能用增強型 for 迴圈 (for-each)
                    Unit unit = units.get(i);
                    if (!unit.isDead()) {
                        System.out.printf("輪到 %s。\n", unit.getStatus());
                        unit.action(); // action 可以在 units.add(...) 新增

                        if (battleOver()) {
                            break outer;
                        }
                    }
                }
            }
        }
    }

    private boolean battleOver() {
        if(troops.getFirst().getUnits().stream().allMatch(u -> u.isDead())){
            System.out.println("你失敗了！");
            return true;
        } else if(troops.get(1).getUnits().stream().allMatch(u -> u.isDead())) {
            System.out.println("你獲勝了！");
            return true;
        }

        return false;
    }
}
