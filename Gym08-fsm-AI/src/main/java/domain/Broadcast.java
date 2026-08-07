package domain;

import java.util.ArrayList;
import java.util.List;

public class Broadcast {
    private Member currentSpeaker;
    private final List<Speak> speaks = new ArrayList<>();

    public boolean isOnAir() {
        return currentSpeaker != null;
    }

    public Member currentSpeaker() {
        return currentSpeaker;
    }

    void setCurrentSpeaker(Member speaker) {
        this.currentSpeaker = speaker;
    }

    void addSpeak(Speak speak) {
        speaks.add(speak);
    }

    public List<Speak> speaks() {
        return speaks;
    }
}
