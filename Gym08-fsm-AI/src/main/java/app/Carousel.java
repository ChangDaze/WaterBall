package app;

/**
 * Cycling reply sequence. Extracted because two states use it, the domain
 * names it, and it carries an invariant (wrap + reset on state entry).
 */
public class Carousel {
    private final String[] replies;
    private int index;

    public Carousel(String... replies) {
        this.replies = replies;
    }

    public String next() {
        String reply = replies[index];
        index = (index + 1) % replies.length;
        return reply;
    }

    public void reset() {
        index = 0;
    }
}
