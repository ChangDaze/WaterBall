package domain;

public record Message(Member author, String content, String[] tags) {
}
