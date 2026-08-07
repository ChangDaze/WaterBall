package domain.events;

import domain.Member;

public record BroadcastStoppedEvent(Member speaker) implements AuthoredEvent {
    @Override
    public Member creator() {
        return speaker;
    }
}
