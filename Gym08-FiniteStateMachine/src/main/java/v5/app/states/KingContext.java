package v5.app.states;

import v5.domain.Member;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * bot knowledge king flow share context
 */
public class KingContext {
    public LocalDateTime questionDeadline;
    public LocalDateTime thanksDeadline;
    public final Map<Member, Integer> scores = new LinkedHashMap<>();

    public void clear(){
        questionDeadline = null;
        thanksDeadline = null;
        scores.clear();
    }
}
