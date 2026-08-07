package app;

import domain.Member;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/** Mutable KnowledgeKing state shared between Questioning, Thanks and the root transitions. */
public class KingContext {
    public LocalDateTime questionDeadline;
    public LocalDateTime thanksDeadline;
    /** Insertion-ordered so earlier scorers win the display slot deterministically. */
    public final Map<Member, Integer> scores = new LinkedHashMap<>();

    public void clear() {
        questionDeadline = null;
        thanksDeadline = null;
        scores.clear();
    }
}
