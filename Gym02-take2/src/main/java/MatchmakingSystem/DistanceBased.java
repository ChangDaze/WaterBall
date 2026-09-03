package MatchmakingSystem;

import java.util.Objects;

public class DistanceBased implements Strategy {
    @Override
    public Individual match(Individual i, Individual[] s) {
        if (i == null || s == null || s.length == 0) {
            return null;
        }

        Individual bestMatch = null;
        double minDistance = Double.MAX_VALUE;

        for (Individual candidate : s) {
            if (candidate == null || Objects.equals(candidate.getId(), i.getId())) {
                continue;
            }

            double distance = calculateDistance(i, candidate);

            if (bestMatch == null ||
                distance < minDistance ||
                (Double.compare(distance, minDistance) == 0 && candidate.getId() < bestMatch.getId())) {
                bestMatch = candidate;
                minDistance = distance;
            }
        }

        return bestMatch;
    }

    private double calculateDistance(Individual i1, Individual i2) {
        int dx = i1.getCoordX() - i2.getCoordX();
        int dy = i1.getCoordY() - i2.getCoordY();
        return Math.sqrt((double) dx * dx + (double) dy * dy);
    }
}
