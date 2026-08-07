package domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

/**
 * Community time. Elapse echoes the amount + unit VERBATIM
 * ("🕑 5 seconds elapsed..."), never the absolute time.
 */
public class Clock {
    private final Consumer<String> printer;
    private LocalDateTime now;

    public Clock(Consumer<String> printer) {
        this.printer = printer;
    }

    public void set(LocalDateTime time) {
        this.now = time;
    }

    public LocalDateTime now() {
        return now;
    }

    /** Whoever mutates, prints (Community contract #8). */
    void elapse(int amount, String unit) {
        now = now.plus(amount, chronoUnit(unit));
        printer.accept("🕑 " + amount + " " + unit + " elapsed...");
    }

    private static ChronoUnit chronoUnit(String unit) {
        return switch (unit) {
            case "seconds" -> ChronoUnit.SECONDS;
            case "minutes" -> ChronoUnit.MINUTES;
            case "hours" -> ChronoUnit.HOURS;
            default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
        };
    }
}
