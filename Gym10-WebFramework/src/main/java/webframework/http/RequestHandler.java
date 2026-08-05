package webframework.http;

/**
 * Everything the server adapter needs to know about what happens to a request.
 *
 * <p>This one-method seam is what keeps the dependency arrow pointing inwards: the adapter compiles
 * against this, not against the pipeline, so the vendor-facing code and the dispatch algorithm can
 * be tested — and replaced — independently of one another.
 */
public interface RequestHandler {

    HttpResponse handle(HttpRequest request);
}
