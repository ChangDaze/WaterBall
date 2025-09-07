package Big2;

public class CardParser {

    public Card[] parseCards(String cardsString) {
        String[] tokens = cardsString.trim().split("\\s+");
        Card[] cards = new Card[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            cards[i] = parseCard(tokens[i]);
        }

        return cards;
    }

    private Card parseCard(String token) {
        char suitChar = token.charAt(0);
        Suit suit = switch (suitChar) {
            case 'C' -> Suit.CLUBS;
            case 'D' -> Suit.DIAMONDS;
            case 'H' -> Suit.HEARTS;
            case 'S' -> Suit.SPADES;
            default -> throw new IllegalArgumentException("未知花色：" + suitChar);
        };

        int start = token.indexOf('[');
        int end = token.indexOf(']');
        if (start < 0 || end < 0 || end <= start) {
            throw new IllegalArgumentException("牌格式錯誤：" + token);
        }
        String rankStr = token.substring(start + 1, end);

        Rank rank = switch (rankStr) {
            case "3" -> Rank.THREE;
            case "4" -> Rank.FOUR;
            case "5" -> Rank.FIVE;
            case "6" -> Rank.SIX;
            case "7" -> Rank.SEVEN;
            case "8" -> Rank.EIGHT;
            case "9" -> Rank.NINE;
            case "10" -> Rank.TEN;
            case "J" -> Rank.JACK;
            case "Q" -> Rank.QUEEN;
            case "K" -> Rank.KING;
            case "A" -> Rank.ACE;
            case "2" -> Rank.TWO;
            default -> throw new IllegalArgumentException("未知點數：" + rankStr);
        };

        return new Card(rank, suit);
    }
}

