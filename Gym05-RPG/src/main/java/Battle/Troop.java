package Battle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Troop {
    private final String name;
    private List<Unit> units;
    private Battle battle;
}
