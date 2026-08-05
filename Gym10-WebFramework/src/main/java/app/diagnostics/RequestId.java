package app.diagnostics;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A per-request identifier, registered with {@code HttpRequestScope}.
 *
 * <p>Exists to make the container's lifecycles observable at runtime rather than only in tests: it
 * counts how many times it has been constructed, so one look at {@code GET /api/diagnostics/scopes}
 * shows whether a scope is behaving.
 */
public final class RequestId {

    private static final AtomicInteger CONSTRUCTED = new AtomicInteger();

    private final String value = UUID.randomUUID().toString();
    private final int constructionNumber = CONSTRUCTED.incrementAndGet();

    public String getValue() {
        return value;
    }

    public int getConstructionNumber() {
        return constructionNumber;
    }
}
