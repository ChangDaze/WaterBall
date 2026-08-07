package v5.app.states;

/**
 * Circular object to choose next sentence need to use.
 * set the circular sentences by input.
 */
public class Carousel {
    private final String[] replies;
    private int index;

    public Carousel(String... replies) {
        this.replies = replies;
    }

    public String next(){
        String reply = replies[index];
        index = (index+1) % replies.length;//circular algorithm
        return reply;
    }

    public void reset() {
        index = 0;
    }
}
