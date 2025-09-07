package Big2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CardPattern {
    private final List<Card> combine;
    private final Card biggestCard;
    private final CardPatternType type;

    public List<Card> getCombine() {
        return combine;
    }

    public Card getBiggestCard() {
        return biggestCard;
    }

    public CardPatternType getType() {
        return type;
    }

    public CardPattern(List<Card> combine, CardPatternType type){
        this.type = type;

        Optional<Card> maxCard = combine.stream().max(Card::compareTo);
        biggestCard = maxCard.orElse(null);
        Collections.sort(combine);
        this.combine = combine;
    }
}
