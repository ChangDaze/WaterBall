package Big2;

import java.util.List;

public class PassInterpreter extends CardPatternInterpreter {
    public PassInterpreter(CardPatternInterpreter next) {
        super(next);
    }

    @Override
    protected boolean match(List<Card> combine) {
        return combine.size() == 0;
    }

    @Override
    protected CardPattern getCardPattern(List<Card> combine) {
        return new CardPattern(combine, CardPatternType.PASS);
    }
}
