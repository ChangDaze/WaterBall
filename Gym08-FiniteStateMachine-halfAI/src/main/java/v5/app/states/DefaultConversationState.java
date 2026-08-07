package v5.app.states;

import v5.domain.Community;
import v5.domain.Member;
import v5.domain.events.NewMessageEvent;
import v5.domain.events.NewPostEvent;
import v5.events.Event;
import v5.fsm.State;

/**
 * part of bot's normal flow
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
        switch (e){
            //bot use carousel reply
            case NewMessageEvent m -> community.sendMessage(self, carousel.next(), new String[]{m.creator().userId()});
            //bot use fix comment and tag creator.
            case NewPostEvent p -> community.addComment(p.post().id(), self, "Nice post @" + p.creator().userId());
            default -> {}
        }
    }
}
