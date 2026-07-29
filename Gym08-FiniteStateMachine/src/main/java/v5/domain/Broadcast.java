package v5.domain;

import java.util.ArrayList;
import java.util.List;

public class Broadcast {
    private Member currentSpeaker;
    private final List<Speak> speaks = new ArrayList<>();

    /**
     * The status that determine if there is somebody is broadcasting.
     * currently use on robot state and meet the input spec.
     */
    public boolean isOnAir() {
        return currentSpeaker != null;
    }

    public Member currentSpeaker() {
        return currentSpeaker;
    }

    /**
     * many action trigger by community only, so the function access would be private.
     * In Java not write out available level is package private different from private.
     */
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
