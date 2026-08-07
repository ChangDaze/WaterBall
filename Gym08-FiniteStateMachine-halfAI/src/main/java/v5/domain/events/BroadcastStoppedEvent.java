package v5.domain.events;

import v5.domain.Member;

public record BroadcastStoppedEvent(Member speaker) implements AuthoredEvent {
    @Override
    public Member creator() {
        return speaker;
    }
}
