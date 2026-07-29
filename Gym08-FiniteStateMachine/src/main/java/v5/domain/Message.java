package v5.domain;

public record Message(Member author, String content, String[] tags) {
}
