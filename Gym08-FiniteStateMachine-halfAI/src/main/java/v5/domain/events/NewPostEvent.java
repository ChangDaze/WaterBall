package v5.domain.events;

import v5.domain.Member;
import v5.domain.Post;

public record NewPostEvent (Post post) implements AuthoredEvent {
    @Override
    public Member creator() {
        return post.author();
    }
}
