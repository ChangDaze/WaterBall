package app.diagnostics;

import java.util.LinkedHashMap;
import java.util.Map;

import app.repository.UserRepository;
import app.service.UserService;

/**
 * {@code GET /api/diagnostics/scopes} — the three lifecycles, visible from a browser.
 *
 * <p>Every claim in milestone 6's acceptance list is answered by this one constructor. Asking for
 * {@code RequestId} twice shows a request-scoped dependency is the same object within a request;
 * asking for {@code UserService} twice shows a prototype is not; and comparing identities across two
 * calls shows the singleton surviving while the request-scoped pair does not.
 *
 * <p>Registered with {@code HttpRequestScope} itself, so the controller is rebuilt per request — the
 * only way a singleton controller could hold a request-scoped dependency is by not being a singleton.
 */
public final class ScopeDiagnosticsController {

    private final RequestId firstRequestId;
    private final RequestId secondRequestId;
    private final UserService firstPrototype;
    private final UserService secondPrototype;
    private final UserRepository singleton;

    public ScopeDiagnosticsController(RequestId firstRequestId, RequestId secondRequestId,
                                      UserService firstPrototype, UserService secondPrototype,
                                      UserRepository singleton) {
        this.firstRequestId = firstRequestId;
        this.secondRequestId = secondRequestId;
        this.firstPrototype = firstPrototype;
        this.secondPrototype = secondPrototype;
        this.singleton = singleton;
    }

    public Map<String, Object> describe() {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("requestScoped_sameInstanceWithinRequest", firstRequestId == secondRequestId);
        report.put("requestScoped_id", firstRequestId.getValue());
        report.put("requestScoped_timesConstructedSinceBoot", firstRequestId.getConstructionNumber());
        report.put("prototype_distinctInstances", firstPrototype != secondPrototype);
        report.put("singleton_identity", System.identityHashCode(singleton));
        report.put("hint", "Call twice: requestScoped_id changes, singleton_identity does not.");
        return report;
    }
}
