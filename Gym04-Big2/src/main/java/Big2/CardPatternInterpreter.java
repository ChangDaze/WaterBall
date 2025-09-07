package Big2;

import java.util.List;

public abstract class CardPatternInterpreter {
    protected final CardPatternInterpreter next;

    public CardPatternInterpreter(CardPatternInterpreter next) {
        this.next = next;
    }

    public CardPattern interprete(List<Card> combine) {
        if(match(combine)) {
            return getCardPattern(combine);
        } else if (next != null) {
            return next.interprete(combine);
        }

        return null;
    }

    protected abstract boolean match(List<Card> combine);

    protected abstract CardPattern getCardPattern(List<Card> combine);
}
