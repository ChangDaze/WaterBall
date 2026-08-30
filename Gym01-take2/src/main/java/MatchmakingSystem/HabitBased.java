package MatchmakingSystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HabitBased implements Strategy {
    @Override
    public Individual match(Individual i, Individual[] s) {
        if (i == null || s == null || s.length == 0) {
            return null;
        }

        Set<String> targetHabits = new HashSet<>();
        if (i.getHabits() != null) {
            targetHabits.addAll(Arrays.asList(i.getHabits()));
        }

        Individual bestMatch = null;
        int maxIntersection = -1;

        for (Individual candidate : s) {
            if (candidate == null || Objects.equals(candidate.getId(), i.getId())) {
                continue;
            }

            int intersectionCount = countHabitIntersection(targetHabits, candidate.getHabits());

            if (bestMatch == null ||
                intersectionCount > maxIntersection ||
                (intersectionCount == maxIntersection && candidate.getId() < bestMatch.getId())) {
                bestMatch = candidate;
                maxIntersection = intersectionCount;
            }
        }

        return bestMatch;
    }

    private int countHabitIntersection(Set<String> targetHabits, String[] candidateHabits) {
        if (candidateHabits == null || targetHabits.isEmpty()) {
            return 0;
        }
        Set<String> uniqueCandidateHabits = new HashSet<>(Arrays.asList(candidateHabits));
        uniqueCandidateHabits.retainAll(targetHabits);
        return uniqueCandidateHabits.size();
    }
}
