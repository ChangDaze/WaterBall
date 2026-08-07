package app;

import domain.Member;

/** Mutable slot shared between the record transitions and RecordingState. */
public class RecordContext {
    public Member recorder;
}
