package v5.domain.events;

import v5.domain.Member;
import v5.events.Event;

public record LogoutEvent(Member member) implements Event {
}
