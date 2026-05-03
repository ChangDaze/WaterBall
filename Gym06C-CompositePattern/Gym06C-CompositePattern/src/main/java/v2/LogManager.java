package v2;

import java.util.HashMap;
import java.util.Map;

public class LogManager {
    private static final Map<String, Logger> loggers = new HashMap<>();

    public static void register(String name, Logger logger) {
        loggers.put(name, logger);
    }

    public static Logger getLogger(String name) {
        // Fallback to root if specific logger isn't found
        return loggers.getOrDefault(name, loggers.get("root"));
    }
}
