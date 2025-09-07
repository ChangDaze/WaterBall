package Big2;

import java.util.List;

public class SingleInterpreter extends CardPatternInterpreter  {
    public SingleInterpreter(CardPatternInterpreter next) {
        super(next);
    }

    @Override
    protected boolean match(List<Card> combine) {
        if (combine.size() != 1) {
            return false;
        }

        return true;
    }

    @Override
    protected CardPattern getCardPattern(List<Card> combine) {
        return new CardPattern(combine, CardPatternType.SINGLE);
    }
}
