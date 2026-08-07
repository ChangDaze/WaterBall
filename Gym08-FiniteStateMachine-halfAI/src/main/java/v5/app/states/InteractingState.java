package v5.app.states;

import v5.domain.Community;
import v5.domain.Member;
import v5.domain.events.NewMessageEvent;
import v5.domain.events.NewPostEvent;
import v5.events.Event;
import v5.fsm.State;

import java.util.stream.Collectors;

/**
 * part of bot's normal flow
 */
public class InteractingState implements State {
    private final Community community;
    private final Member self;
    private final Carousel carousel = new Carousel("Hi hi😁", "I like your idea!");

    public InteractingState(Community community, Member self) {
        this.community = community;
        this.self = self;
    }

    @Override
    public void onEnter() {
        carousel.reset();
    }

    @Override
    public void handle(Event e) {
        //generation can use switch to chose
        switch (e){
            //bot use carousel reply
            case NewMessageEvent m -> community.sendMessage(self, carousel.next(), new String[]{m.creator().userId()});
            //bot tag all online member to ask about thought, but this tag currently no real affection.
            case NewPostEvent p -> {
                String mentions = community.onlineMembers().stream()
                        .map((member -> "@" + member.userId()))
                        .collect(Collectors.joining(", "));
                community.addComment(p.post().id(), self, "How do you guys think about it? " + mentions);
            }
            default -> {}
        }
    }
}
