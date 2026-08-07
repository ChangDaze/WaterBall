package domain.events;

import domain.Member;

public record GoBroadcastingEvent(Member speaker) implements AuthoredEvent {
    @Override
    public Member creator() {
        return speaker;
    }
}
