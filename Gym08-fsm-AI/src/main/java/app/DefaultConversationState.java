package app;

import domain.Community;
import domain.Member;
import domain.events.NewMessageEvent;
import domain.events.NewPostEvent;
import events.Event;
import fsm.State;

/**
 * Normal mode, small crowd (< 10 online). Carousel-replies to every message;
 * comments "Nice post" on every new post.
 */
public class DefaultConversationState implements State {
    private final Community community;
    private final Member self;
    private final Carousel carousel = new Carousel("good to hear", "thank you", "How are you");

    public DefaultConversationState(Community community, Member self) {
        this.community = community;
        this.self = self;
    }

    @Override
    public void onEnter() {
        carousel.reset();
    }

    @Override
    public void handle(Event e) {
        switch (e) {
            case NewMessageEvent m ->
                    community.sendMessage(self, carousel.next(), new String[]{m.creator().userId()});
            case NewPostEvent p ->
                    community.addComment(p.post().id(), self, "Nice post @" + p.creator().userId());
            default -> {
            }
        }
    }
}
