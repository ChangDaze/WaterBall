package app;

import webframework.WebApplication;
import webframework.routing.Route;

/**
 * Boots the member system.
 *
 * <pre>
 * mvn -q compile exec:java              # port 8080
 * mvn -q compile exec:java -Dexec.args=9090
 * </pre>
 */
public final class Main {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        int port = resolvePort(args);
        WebApplication app = MemberSystemApplication.create();
        app.start(port);

        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));

        System.out.println("Member system listening on http://localhost:" + app.getPort());
        System.out.println("Routes:");
        for (Route route : app.getRouter().getRoutes()) {
            System.out.println("  " + route);
        }
        System.out.println("Try: curl -i -X POST localhost:" + app.getPort() + "/hello");
        System.out.println("Stop with Ctrl+C.");
    }

    /**
     * @param args optionally a single port number; falls back to the {@code PORT} environment
     *             variable, then to 8080
     */
    private static int resolvePort(String[] args) {
        String candidate = args.length > 0 ? args[0] : System.getenv("PORT");
        if (candidate == null || candidate.isBlank()) {
            return DEFAULT_PORT;
        }
        try {
            return Integer.parseInt(candidate.trim());
        } catch (NumberFormatException e) {
            System.err.println("Ignoring unusable port \"" + candidate + "\"; using " + DEFAULT_PORT);
            return DEFAULT_PORT;
        }
    }
}
