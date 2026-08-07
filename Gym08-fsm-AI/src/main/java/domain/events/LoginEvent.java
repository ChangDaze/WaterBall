package domain.events;

import domain.Member;
import events.Event;

public record LoginEvent(Member member) implements Event {
}
