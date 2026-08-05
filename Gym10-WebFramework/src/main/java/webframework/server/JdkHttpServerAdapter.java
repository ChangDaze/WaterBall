package webframework.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import webframework.http.HttpRequest;
import webframework.http.HttpResponse;
import webframework.http.HttpStatus;
import webframework.http.RequestHandler;
import webframework.serialization.SerializerRegistry;

/**
 * Owns the JDK server and performs step 1 and step 8 of the pipeline: adapt the incoming exchange,
 * and write the finished response back out.
 *
 * <p>BUILD_SPEC B: exactly <em>one</em> {@code HttpHandler} is registered, at {@code "/"} — the Front
 * Controller. Dispatch happens inside the framework, so no application code ever switches on a path.
 */
public final class JdkHttpServerAdapter {

    private final RequestHandler requestHandler;
    private final SerializerRegistry serializers;

    private HttpServer server;
    private ExecutorService executor;

    public JdkHttpServerAdapter(RequestHandler requestHandler, SerializerRegistry serializers) {
        this.requestHandler = requestHandler;
        this.serializers = serializers;
    }

    /**
     * @param port the port to bind, or 0 to let the OS choose one (see {@link #getPort()})
     */
    public void start(int port) {
        if (server != null) {
            throw new IllegalStateException("Server is already started on port " + getPort());
        }
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot bind port " + port, e);
        }
        server.createContext("/", this::handleExchange);
        executor = Executors.newFixedThreadPool(
                Math.max(2, Runtime.getRuntime().availableProcessors()));
        server.setExecutor(executor);
        server.start();
    }

    public int getPort() {
        if (server == null) {
            throw new IllegalStateException("Server is not started");
        }
        return server.getAddress().getPort();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
        }
    }

    private void handleExchange(HttpExchange exchange) throws IOException {
        try {
            write(exchange, respondTo(exchange));
        } finally {
            exchange.close();
        }
    }

    private HttpResponse respondTo(HttpExchange exchange) {
        try {
            // Step 1: the vendor type stops here and never travels further inwards.
            HttpRequest request = new JdkHttpRequest(exchange, serializers);
            return requestHandler.handle(request);
        } catch (Throwable failure) {
            // Reached only when the request could not be adapted at all — an unknown HTTP verb, say.
            // Past this point the pipeline owns error handling; this is the socket's own last resort.
            String message = failure.getMessage() != null
                    ? failure.getMessage()
                    : failure.getClass().getName();
            return HttpResponse.text(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

    /**
     * Step 8. The body-less branch is BUILD_SPEC 2.3 case 3: length {@code -1} tells the JDK to send
     * no body, and because {@link HttpResponse#noContent()} carries no headers, no
     * {@code content-type} is emitted either.
     */
    private void write(HttpExchange exchange, HttpResponse response) throws IOException {
        response.getHeaders().forEach((name, value) -> exchange.getResponseHeaders().set(name, value));
        int status = response.getStatus().getCode();
        if (!response.hasBody()) {
            exchange.sendResponseHeaders(status, -1);
            return;
        }
        byte[] bytes = response.getBody().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
