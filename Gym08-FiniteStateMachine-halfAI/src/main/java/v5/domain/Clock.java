package v5.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

/**
 * Clock is a simple timekeeper that extend the ability of the community.
 */
public class Clock {
    private final Consumer<String> printer;
    private LocalDateTime now;

    public Clock(Consumer<String> printer) {
        this.printer = printer;
    }

    public void set(LocalDateTime time){
        this.now = time;
    }

    public LocalDateTime now() {
        return now;
    }

    /**
     * help community to output the time passed.
     */
    void elapse(int amount, String unit) {
        now = now.plus(amount, chronoUnit(unit));
        printer.accept("🕑 " + amount + " " + unit + " elapsed...");
    }

    static ChronoUnit chronoUnit(String unit) {
        return switch (unit){
            case "seconds" -> ChronoUnit.SECONDS;
            case "minutes" -> ChronoUnit.MINUTES;
            case "hours" -> ChronoUnit.HOURS;
            default -> throw new IllegalArgumentException("Unsupported time unit: " + unit);
        };
    }
}
