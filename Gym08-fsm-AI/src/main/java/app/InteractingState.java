package app;

import domain.Community;
import domain.Member;
import domain.events.NewMessageEvent;
import domain.events.NewPostEvent;
import events.Event;
import fsm.State;

import java.util.stream.Collectors;

/**
 * Normal mode, big crowd (>= 10 online). Carousel-replies to messages; on a
 * new post it comments, rallying everyone online (login order, bot included).
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
        switch (e) {
            case NewMessageEvent m ->
                    community.sendMessage(self, carousel.next(), new String[]{m.creator().userId()});
            case NewPostEvent p -> {
                String mentions = community.onlineMembers().stream()
                        .map(member -> "@" + member.userId())
                        .collect(Collectors.joining(", "));
                community.addComment(p.post().id(), self, "How do you guys think about it? " + mentions);
            }
            default -> {
            }
        }
    }
}
