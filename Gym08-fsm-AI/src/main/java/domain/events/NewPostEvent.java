package domain.events;

import domain.Member;
import domain.Post;

public record NewPostEvent(Post post) implements AuthoredEvent {
    @Override
    public Member creator() {
        return post.author();
    }
}
