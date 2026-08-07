package events;

/**
 * Port through which events cross a boundary (domain → bots, etc.).
 */
public interface EventListener {
    void onEvent(Event e);
}
