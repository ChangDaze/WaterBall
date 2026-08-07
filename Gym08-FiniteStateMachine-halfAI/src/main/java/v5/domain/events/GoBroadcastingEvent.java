package v5.domain.events;

import v5.domain.Member;

public record GoBroadcastingEvent(Member speaker) implements AuthoredEvent {
    @Override
    public Member creator() {
        return speaker;
    }
}
