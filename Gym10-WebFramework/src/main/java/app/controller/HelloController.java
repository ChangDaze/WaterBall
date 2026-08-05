package app.controller;

/**
 * The walking skeleton from milestone 1, kept as a dependency-free smoke test:
 * {@code curl -X POST localhost:8080/hello}.
 *
 * <p>Also the one handler registered with no {@code HttpRequest} parameter, which is what proves the
 * framework supports that shape.
 */
public final class HelloController {

    public String hello() {
        return "Hello, framework!";
    }
}
