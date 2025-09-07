package Big2;

import java.util.List;

public class PairInterpreter extends CardPatternInterpreter {
    public PairInterpreter(CardPatternInterpreter next) {
        super(next);
    }

    @Override
    protected boolean match(List<Card> combine) {
        if (combine.size() != 2) {
            return false;
        }

        return combine.get(0).getRank() == combine.get(1).getRank();
    }

    @Override
    protected CardPattern getCardPattern(List<Card> combine) {
        return new CardPattern(combine, CardPatternType.PAIR);
    }
}
