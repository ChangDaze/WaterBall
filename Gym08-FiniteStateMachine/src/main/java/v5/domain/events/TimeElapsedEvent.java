package v5.domain.events;

import v5.events.Event;

public record TimeElapsedEvent(int amount, String unit) implements Event {
}
