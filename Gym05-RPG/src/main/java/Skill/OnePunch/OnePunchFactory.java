package Skill.OnePunch;

public class OnePunchFactory {
    public static OnePunch create() {
        return HighHpOnePunch.builder()
                .next(PoisonedPetrochemicalOnePunch.builder()
                        .next(CheeredupOnePunch.builder()
                                .next(NormalOnePunch.builder().build())
                                .build())
                        .build())
                .build();
    }
}
