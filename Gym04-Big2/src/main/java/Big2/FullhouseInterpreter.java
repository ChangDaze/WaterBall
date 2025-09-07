package Big2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FullhouseInterpreter extends CardPatternInterpreter  {
    public FullhouseInterpreter(CardPatternInterpreter next) {
        super(next);
    }

    @Override
    protected boolean match(List<Card> combine) {
        if (combine.size() != 5) {
            return false;  // Full House 必須是 5 張牌
        }

        // 統計每個 Rank 的數量
        Map<Rank, Long> rankCount = combine.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // Full House 條件：有兩組 rank，且其中一組數量是3，另一組數量是2
        return rankCount.size() == 2 &&
                rankCount.containsValue(3L) &&
                rankCount.containsValue(2L);
    }

    @Override
    protected CardPattern getCardPattern(List<Card> combine) {
        return new CardPattern(combine, CardPatternType.FULL_HOUSE);
    }
}
