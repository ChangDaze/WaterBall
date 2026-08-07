package domain.events;

import events.Event;

public record TimeElapsedEvent(int amount, String unit) implements Event {
}
