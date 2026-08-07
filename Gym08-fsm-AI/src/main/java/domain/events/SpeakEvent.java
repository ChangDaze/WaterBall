package domain.events;

import domain.Member;
import domain.Speak;

public record SpeakEvent(Speak speak) implements AuthoredEvent {
    @Override
    public Member creator() {
        return speak.speaker();
    }
}
