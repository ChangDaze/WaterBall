import java.time.Duration;

public class Video {
    private final String title;
    private final String Description;
    private final Duration Length;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return Description;
    }

    public Duration getLength() {
        return Length;
    }

    public Video(String title, String description, Duration length) {
        this.title = title;
        this.Description = description;
        this.Length = length;
    }
}
