package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import webframework.WebApplication;

/**
 * A1–A4 over a real socket, against the same wiring {@code Main} boots.
 *
 * <p>A fresh application per test, on an OS-assigned port, so the in-memory store starts empty and
 * ids are predictable.
 */
class MemberSystemEndToEndTest {

    private WebApplication app;
    private HttpClient client;
    private String base;

    @BeforeEach
    void startServer() {
        app = MemberSystemApplication.create();
        app.start(0);
        base = "http://localhost:" + app.getPort();
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    }

    @AfterEach
    void stopServer() {
        app.stop();
    }

    // ---------------------------------------------------------------- A1 register

    @Test
    @DisplayName("A1: registering returns 201 and the new id, counting from 1")
    void registerSucceeds() throws Exception {
        HttpResponse<String> first = register("amy@example.com", "Amy Adams", "secret1");
        HttpResponse<String> second = register("bob@example.com", "Bobby Brown", "secret2");

        assertEquals(201, first.statusCode());
        assertEquals("application/json", contentTypeOf(first));
        assertEquals(1, json(first).get("id").getAsInt());
        assertEquals(2, json(second).get("id").getAsInt());
    }

    @Test
    @DisplayName("A1: a duplicate email is 400 \"Duplicate email\"")
    void duplicateEmailIsRejected() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");

        HttpResponse<String> response = register("amy@example.com", "Amy Again", "secret9");

        assertEquals(400, response.statusCode());
        assertEquals("Duplicate email", response.body());
        assertEquals("text/plain", contentTypeOf(response));
    }

    @Test
    @DisplayName("A1: every kind of malformed field is 400 \"Registration's format incorrect.\"")
    void registrationFormatIsChecked() throws Exception {
        assertEquals("Registration's format incorrect.",
                register("cat@example.com", "Cat", "secret1").body(), "name shorter than 5");
        assertEquals("Registration's format incorrect.",
                register("not-an-email", "Valid Name", "secret1").body(), "not an email");
        assertEquals("Registration's format incorrect.",
                register("dog@example.com", "Valid Name", "abc").body(), "password shorter than 5");
        assertEquals("Registration's format incorrect.",
                register("dog@example.com", "Valid Name", "x".repeat(33)).body(), "password over 32");

        HttpResponse<String> shortName = register("cat@example.com", "Cat", "secret1");
        assertEquals(400, shortName.statusCode());
        assertEquals("text/plain", contentTypeOf(shortName));
    }

    // ---------------------------------------------------------------- A2 login

    @Test
    @DisplayName("A2: logging in returns 200 and a token of 36–60 characters")
    void loginSucceeds() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");

        HttpResponse<String> response = login("amy@example.com", "secret1");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", contentTypeOf(response));
        String token = json(response).get("token").getAsString();
        assertTrue(token.length() >= 36 && token.length() <= 60,
                "token length was " + token.length());
    }

    @Test
    @DisplayName("A2: each login issues a different token")
    void tokensAreUnique() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");

        assertNotEquals(tokenFor("amy@example.com", "secret1"),
                tokenFor("amy@example.com", "secret1"));
    }

    @Test
    @DisplayName("A2: a wrong password or unknown email is 400 \"Credentials Invalid\"")
    void badCredentialsAreRejected() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");

        HttpResponse<String> wrongPassword = login("amy@example.com", "wrongpw");
        HttpResponse<String> unknownEmail = login("zoe@example.com", "secret1");

        assertEquals(400, wrongPassword.statusCode());
        assertEquals("Credentials Invalid", wrongPassword.body());
        assertEquals(400, unknownEmail.statusCode());
        assertEquals("Credentials Invalid", unknownEmail.body());
    }

    @Test
    @DisplayName("A2: a malformed login body is 400 \"Login's format incorrect.\"")
    void loginFormatIsChecked() throws Exception {
        HttpResponse<String> response = send(
                request("/api/users/login").header("content-type", "application/json")
                        .POST(BodyPublishers.ofString("{\"email\":\"amy@example.com\"}")));

        assertEquals(400, response.statusCode());
        assertEquals("Login's format incorrect.", response.body());
    }

    // ---------------------------------------------------------------- A3 rename

    @Test
    @DisplayName("A3: renaming yourself is 204 with no body and no content-type header")
    void renameSucceedsWith204() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");
        String token = tokenFor("amy@example.com", "secret1");

        HttpResponse<String> response = rename(1, token, "Renamed Amy");

        assertEquals(204, response.statusCode());
        assertEquals("", response.body());
        // BUILD_SPEC 2.3 case 3, asserted on the wire.
        assertTrue(response.headers().firstValue("content-type").isEmpty(),
                "a 204 must carry no content-type header, got "
                        + response.headers().firstValue("content-type").orElse(""));

        assertTrue(get("/api/users").body().contains("Renamed Amy"));
    }

    @Test
    @DisplayName("A3: a missing, malformed or unknown token is 401")
    void renameNeedsAValidToken() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");
        String expected = "Can't authenticate who you are.";

        HttpResponse<String> noHeader = send(request("/api/users/1")
                .header("content-type", "application/json")
                .method("PATCH", BodyPublishers.ofString("{\"name\":\"Renamed Amy\"}")));
        HttpResponse<String> notBearer = rename(1, null, "Renamed Amy", "Basic abc");
        HttpResponse<String> unknownToken = rename(1, "no-such-token", "Renamed Amy");

        assertEquals(401, noHeader.statusCode());
        assertEquals(expected, noHeader.body());
        assertEquals(401, notBearer.statusCode());
        assertEquals(expected, notBearer.body());
        assertEquals(401, unknownToken.statusCode());
        assertEquals(expected, unknownToken.body());
    }

    @Test
    @DisplayName("A3: renaming somebody else is 403 \"Forbidden\"")
    void renamingAnotherUserIsForbidden() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");
        register("bob@example.com", "Bobby Brown", "secret2");
        String amysToken = tokenFor("amy@example.com", "secret1");

        HttpResponse<String> response = rename(2, amysToken, "Hijacked Name");

        assertEquals(403, response.statusCode());
        assertEquals("Forbidden", response.body());
    }

    @Test
    @DisplayName("A3: a name outside 5–32 is 400 \"Name's format invalid.\"")
    void renameChecksTheName() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");
        String token = tokenFor("amy@example.com", "secret1");

        HttpResponse<String> tooShort = rename(1, token, "Amy");
        HttpResponse<String> tooLong = rename(1, token, "x".repeat(33));

        assertEquals(400, tooShort.statusCode());
        assertEquals("Name's format invalid.", tooShort.body());
        assertEquals(400, tooLong.statusCode());
        assertEquals("Name's format invalid.", tooLong.body());
    }

    @Test
    @DisplayName("A3: 403 is decided before the body's own 400")
    void authorizationIsCheckedBeforeTheBody() throws Exception {
        // Renaming somebody else with an invalid name must report the forbidden act, not the name.
        register("amy@example.com", "Amy Adams", "secret1");
        register("bob@example.com", "Bobby Brown", "secret2");
        String amysToken = tokenFor("amy@example.com", "secret1");

        HttpResponse<String> response = rename(2, amysToken, "No");

        assertEquals(403, response.statusCode());
        assertEquals("Forbidden", response.body());
    }

    // ---------------------------------------------------------------- A4 query

    @Test
    @DisplayName("A4: with no keyword every member is returned, and never a password")
    void queryReturnsEveryone() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");
        register("bob@example.com", "Bobby Brown", "secret2");

        HttpResponse<String> response = get("/api/users");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", contentTypeOf(response));
        JsonArray users = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(2, users.size());
        JsonObject amy = users.get(0).getAsJsonObject();
        assertEquals(1, amy.get("id").getAsInt());
        assertEquals("amy@example.com", amy.get("email").getAsString());
        assertEquals("Amy Adams", amy.get("name").getAsString());
        assertFalse(response.body().contains("password"), "A4 must never expose passwords");
        assertFalse(response.body().contains("secret1"));
    }

    @Test
    @DisplayName("A4: a keyword filters by name substring")
    void queryFiltersByKeyword() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");
        register("bob@example.com", "Bobby Brown", "secret2");

        JsonArray matching = JsonParser.parseString(get("/api/users?keyword=Bob").body())
                .getAsJsonArray();
        JsonArray empty = JsonParser.parseString(get("/api/users?keyword=zzz").body())
                .getAsJsonArray();

        assertEquals(1, matching.size());
        assertEquals("Bobby Brown", matching.get(0).getAsJsonObject().get("name").getAsString());
        assertEquals(0, empty.size());
    }

    // ---------------------------------------------------------------- framework behaviour

    @Test
    @DisplayName("D: an unknown path is 404 with the exact text")
    void unknownPathIs404() throws Exception {
        HttpResponse<String> response = get("/api/nope");

        assertEquals(404, response.statusCode());
        assertEquals("Cannot find the path \"/api/nope\"", response.body());
        assertEquals("text/plain", contentTypeOf(response));
    }

    @Test
    @DisplayName("D: a known path with the wrong method is 405 with the exact text")
    void wrongMethodIs405() throws Exception {
        HttpResponse<String> response = send(request("/api/users").DELETE());

        assertEquals(405, response.statusCode());
        assertEquals("The method \"DELETE\" is not allowed on \"/api/users\"", response.body());
        assertEquals("text/plain", contentTypeOf(response));
    }

    @Test
    @DisplayName("C: an unsupported request content-type is 500")
    void unsupportedRequestContentTypeIs500() throws Exception {
        HttpResponse<String> response = send(request("/api/users")
                .header("content-type", "application/yaml")
                .POST(BodyPublishers.ofString("email: amy@example.com")));

        assertEquals(500, response.statusCode());
        assertEquals("Unsupported media type \"application/yaml\"", response.body());
        assertEquals("text/plain", contentTypeOf(response));
    }

    @Test
    @DisplayName("M1: the walking-skeleton route still answers, as plain text")
    void helloStillWorks() throws Exception {
        HttpResponse<String> response = send(request("/hello").POST(BodyPublishers.noBody()));

        assertEquals(200, response.statusCode());
        assertEquals("Hello, framework!", response.body());
        assertEquals("text/plain", contentTypeOf(response));
    }

    // ---------------------------------------------------------------- E: the XML plugin

    @Test
    @DisplayName("E: a route can serialize as XML, added entirely by a plugin")
    void xmlResponsesComeFromThePlugin() throws Exception {
        register("amy@example.com", "Amy Adams", "secret1");

        HttpResponse<String> response = get("/api/users/xml");

        assertEquals(200, response.statusCode());
        assertEquals("application/xml", contentTypeOf(response));
        assertTrue(response.body().contains("<name>Amy Adams</name>"), response.body());
        assertFalse(response.body().contains("password"));
    }

    @Test
    @DisplayName("E: an XML request body is deserialized by content-type, on an unchanged route")
    void xmlRequestBodiesAreAccepted() throws Exception {
        HttpResponse<String> response = send(request("/api/users")
                .header("content-type", "application/xml")
                .POST(BodyPublishers.ofString("<registerRequest>"
                        + "<email>xml@example.com</email>"
                        + "<name>Xavier Xml</name>"
                        + "<password>secret4</password>"
                        + "</registerRequest>", StandardCharsets.UTF_8)));

        assertEquals(201, response.statusCode());
        assertEquals(1, json(response).get("id").getAsInt());
        assertTrue(get("/api/users").body().contains("Xavier Xml"));
    }

    @Test
    @DisplayName("E: a DTO validates identically whichever media type delivered it")
    void xmlRequestBodiesAreValidatedToo() throws Exception {
        HttpResponse<String> response = send(request("/api/users")
                .header("content-type", "application/xml")
                .POST(BodyPublishers.ofString("<registerRequest><email>bad</email>"
                        + "<name>No</name><password>p</password></registerRequest>")));

        assertEquals(400, response.statusCode());
        assertEquals("Registration's format incorrect.", response.body());
    }

    // ---------------------------------------------------------------- F: lifecycles, live

    @Test
    @DisplayName("F: a request-scoped instance is shared within a request and gone by the next")
    void lifecyclesBehaveOverHttp() throws Exception {
        JsonObject first = JsonParser.parseString(get("/api/diagnostics/scopes").body())
                .getAsJsonObject();
        JsonObject second = JsonParser.parseString(get("/api/diagnostics/scopes").body())
                .getAsJsonObject();

        assertTrue(first.get("requestScoped_sameInstanceWithinRequest").getAsBoolean());
        assertTrue(first.get("prototype_distinctInstances").getAsBoolean());
        assertNotEquals(first.get("requestScoped_id").getAsString(),
                second.get("requestScoped_id").getAsString(),
                "a new request must get a new request-scoped instance");
        assertEquals(first.get("singleton_identity").getAsInt(),
                second.get("singleton_identity").getAsInt(),
                "the singleton must survive across requests");
    }

    // ---------------------------------------------------------------- helpers

    private HttpRequest.Builder request(String path) {
        return HttpRequest.newBuilder(URI.create(base + path)).timeout(Duration.ofSeconds(10));
    }

    private HttpResponse<String> send(HttpRequest.Builder builder)
            throws IOException, InterruptedException {
        return client.send(builder.build(), BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        return send(request(path).GET());
    }

    private HttpResponse<String> register(String email, String name, String password)
            throws IOException, InterruptedException {
        String body = "{\"email\":\"%s\",\"name\":\"%s\",\"password\":\"%s\"}"
                .formatted(email, name, password);
        return send(request("/api/users").header("content-type", "application/json")
                .POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)));
    }

    private HttpResponse<String> login(String email, String password)
            throws IOException, InterruptedException {
        String body = "{\"email\":\"%s\",\"password\":\"%s\"}".formatted(email, password);
        return send(request("/api/users/login").header("content-type", "application/json")
                .POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)));
    }

    private String tokenFor(String email, String password) throws IOException, InterruptedException {
        return json(login(email, password)).get("token").getAsString();
    }

    private HttpResponse<String> rename(int userId, String token, String name)
            throws IOException, InterruptedException {
        return rename(userId, token, name, token == null ? null : "Bearer " + token);
    }

    private HttpResponse<String> rename(int userId, String token, String name, String authorization)
            throws IOException, InterruptedException {
        HttpRequest.Builder builder = request("/api/users/" + userId)
                .header("content-type", "application/json");
        if (authorization != null) {
            builder = builder.header("Authorization", authorization);
        }
        return send(builder.method("PATCH", BodyPublishers.ofString(
                "{\"name\":\"%s\"}".formatted(name), StandardCharsets.UTF_8)));
    }

    private static JsonObject json(HttpResponse<String> response) {
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }

    private static String contentTypeOf(HttpResponse<String> response) {
        return response.headers().firstValue("content-type").orElse(null);
    }
}
