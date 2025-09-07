package Big2;

import java.util.*;
import java.util.stream.Collectors;

public class StraightInterpreter extends CardPatternInterpreter{
    public StraightInterpreter(CardPatternInterpreter next) {
        super(next);
    }

    @Override
    protected boolean match(List<Card> combine) {
        if (combine.size() != 5) {
            return false;  // 順子至少5張
        }

        //判斷順子時，不能從最大或最小的牌（像3或2）直接當起點去判斷連續性，因為順子是環狀的，起點可以在任何牌上。
        //所以每張牌都要當作起點檢查過
        Collections.sort(combine);
        int size = combine.size();
        for (int start = 0; start < size; start++) {
            boolean isStraight = true;

            for (int i = 0; i < size - 1; i++) {
                int currentIndex = (start + i) % size;
                int nextIndex = (start + i + 1) % size;

                int currVal = combine.get(currentIndex).getRank().getValue();
                int nextVal = combine.get(nextIndex).getRank().getValue();

                // 環狀判斷點數差是否為1 (mod 13)
                if ((nextVal - currVal + 13) % 13 != 1) {
                    isStraight = false;
                    break;
                }
            }

            if (isStraight)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    protected CardPattern getCardPattern(List<Card> combine) {
        return new CardPattern(combine, CardPatternType.STRAIGHT);
    }
}
