package v5.domain.events;

import v5.domain.Member;
import v5.domain.Speak;

public record SpeakEvent(Speak speak) implements AuthoredEvent {
    @Override
    public Member creator() {
        return speak.speaker();
    }
}
