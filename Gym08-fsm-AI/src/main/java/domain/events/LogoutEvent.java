package domain.events;

import domain.Member;
import events.Event;

public record LogoutEvent(Member member) implements Event {
}
