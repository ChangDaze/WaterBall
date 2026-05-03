package v1;

public enum Level {
    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);

    private final int _priority;

    Level(int priority) {
        this._priority = priority;
    }

    public int getPriority() {
        return _priority;
    }
}
