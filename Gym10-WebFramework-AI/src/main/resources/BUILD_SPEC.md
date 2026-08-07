# Build Spec — 自幹一個 Web Framework

> **How to use this document:** paste it whole into Claude Code (or Claude with file creation) as the
> opening prompt. It is written to be self-contained: the requirements, the design decisions that are
> already settled, the exact API contract, and the build order. Work milestone by milestone and keep
> the project runnable at every checkpoint.

---

## 0. Task

Implement a small web framework in **Java 17+**, using only the JDK's built-in
`com.sun.net.httpserver.HttpServer` as the underlying HTTP server. Then use that framework to build a
member system as a proof of concept.

**Hard constraints:**

- No Spring, no Jakarta/Javax servlet, no Jersey, no Micronaut, no existing web framework.
- No dependency-injection library. The IoC container is part of what we are building.
- A JSON library is allowed (Jackson or Gson) — pick one and note it. Everything else stdlib.
- Build with Maven or Gradle; single-command run (`mvn exec:java` or equivalent) must start the app.
- The framework must not import anything from the member system. Dependency direction is strictly
  `app → framework → jdk`.

**An OOA/OOD design already exists for this** (class diagram + sequence diagram). The design
decisions in §2 are settled and must be honoured — they are not open for re-litigation during
implementation. If the code seems to want a different shape, flag it rather than silently diverging.

---

## 1. Requirements

### [B] Routing

Developers register routes declaratively at boot:

```java
router.post("/api/users",              UserController.class, "register");
router.post("/api/users/login",        UserController.class, "login");
router.patch("/api/users/{userId}",    UserController.class, "rename");
router.get("/api/users",               UserController.class, "queryUsers");
```

- Support `GET POST PUT PATCH DELETE` registration methods.
- Path templates support variables in braces: `/api/users/{userId}`.
- The framework registers exactly **one** `HttpHandler` with the JDK server (Front Controller) and
  dispatches internally. No switch-case on paths in user code.

### [C] Serialization / deserialization

- Handlers read the request body as an object: `request.readBodyAsObject(RegisterRequest.class)`.
- The framework picks the deserializer by the request's `content-type` header.
- The framework picks the serializer for the response by the **route's configured response type**.
- Built in: `application/json`, `text/plain`.
- Unsupported request `content-type` → HTTP 500.

### [D] Exception handling

Three built-in cases:

| situation | status | body (exact text) |
|---|---|---|
| no route matches the path | 404 | `Cannot find the path "<path>"` |
| path matches, method does not | 405 | `The method "<METHOD>" is not allowed on "<path>"` |
| handler throws | 500 | the exception's message |

All error responses use `content-type: text/plain`.

### [E] Extensibility — no core modification

Developers must be able to add the following **without editing framework source**:

- a new media type + its serializer (demonstrate with an XML plugin)
- a new exception → status mapping (e.g. `InvalidCredentialsException → 400`)
- a new dependency lifecycle

Plugin API:

```java
app.addPlugin(new XmlMediaTypePlugin());
```

### [F] IoC container

```java
container.register(UserController.class);                              // singleton (default)
container.register(UserService.class,        new PrototypeScope());    // new instance每次
container.register(DatabaseConnection.class, new HttpRequestScope());  // 每個 HTTP 請求一份
Object o = container.get(UserController.class);
```

- Lazy: nothing is constructed until first `get()`.
- Constructor injection, resolved recursively.
- **New lifecycles must be addable without modifying `Container`.**
- Lookup by type and by name.

---

## 2. Settled design decisions — honour these

These came out of the OOA/OOD stage. Each one exists because of a specific conflict in the
requirements; do not "simplify" them away.

### 2.1 `Route` stores the handler's **type and method name**, never a bound instance

The obvious registration style `router.post(path, userController::login)` captures an instance built
at boot. If Route holds that instance, the Container is never consulted, so **every lifecycle in [F]
becomes unreachable** for handlers.

Therefore registration takes `(path, Class<?> handlerType, String methodName)`, and per request the
Router asks the Container for an instance. Use reflection to invoke.

### 2.2 Path variables are written into the request after a match is confirmed

`{userId}` is not part of HTTP. The binding `userId = 3` only exists once a specific Route has
matched. So:

- `Route.matchesPath(request)` returns a **`PathMatchResult`** (`matched` + `pathVariables`), not a
  boolean — otherwise the extracted bindings are computed and thrown away.
- After the winning route is found (and only then, exactly once), the Router calls
  `request.setPathVariables(...)`.
- Accepted trade-off: `HttpRequest` is mutable during dispatch. Exactly one collaborator writes,
  exactly once, before the handler runs. Do not add other mutators.

### 2.3 Response `content-type` ownership, in priority order

1. success **with** a body → the route's configured response media type
2. exception path → `text/plain`, status from the matched `ExceptionRule`
3. **204** → no body **and no `content-type` header at all**

Case 3 is why the route's response media type is optional (`0..1`). Do not emit a `content-type` on a
204.

### 2.4 Deserialization must not swallow application exception types

`readBodyAsObject()` can fail two ways with two different statuses:

- unsupported `content-type` → 500
- the DTO's own validation rejecting input (e.g. bad email) → 400

Do **not** wrap everything from that call in a generic `DeserializationException` — the exception
resolver matches on concrete type, so wrapping turns every 400 into a 500.

### 2.5 The vendor HTTP API is confined to one class

`com.sun.net.httpserver.*` may be imported by **exactly one** production class
(`JdkHttpRequest` / the server adapter). Everything above it sees only the framework's own
`HttpRequest` interface. This is what makes the pipeline unit-testable without sockets.

### 2.6 Request-scope cleanup is exception-safe

The request scope is opened before dispatch and closed in a `finally`. If it is only closed on the
happy path, request-scoped instances leak across requests. This is the most common bug in
hand-rolled containers — get it right the first time.

---

## 3. Structure

Start with two packages. Split further only when a package exceeds ~12 files.

```
src/main/java/
  webframework/
    WebApplication.java          boot entry; owns Router, Container, both registries, plugins
    RequestPipeline.java         the fixed 8-step dispatch algorithm
    RequestContext.java          per-request state; opens/closes the request scope

    HttpRequest.java             INTERFACE  (getHeader, getPathVariable, getQueryParameter,
                                             setPathVariables, readBodyAsObject)
    HttpResponse.java            status, headers, body
    HttpMethod.java              enum: GET POST PUT PATCH DELETE HEAD OPTIONS TRACE CONNECT
    HttpStatus.java              200 201 204 400 401 403 404 405 500
    MediaType.java               value object: type + subtype; parse("application/json")

    JdkHttpServerAdapter.java    starts the JDK server, registers ONE handler
    JdkHttpRequest.java          ADAPTER — the only class importing com.sun.net.httpserver

    Router.java                  get/post/put/patch/delete registration; handle(request)
    Route.java                   matchesPath, matchesMethod, getResponseType, getHandlerMethod
    PathPattern.java             template → segments; match(path) : PathMatchResult
    PathSegment.java             text + isVariable
    PathMatchResult.java         matched + pathVariables
    HandlerMethod.java           handlerType + methodName; invoke(instance, request) : Object

    BodySerializer.java          INTERFACE — Strategy
    SerializerRegistry.java      register / findByMediaType
    JsonBodySerializer.java
    PlainTextBodySerializer.java

    ExceptionRule.java           INTERFACE or class w/ matches(Throwable)
    ExceptionRuleRegistry.java   register / resolve(Throwable) : ExceptionRule
    DefaultExceptionRule.java    the always-matching 500 fallback

    Plugin.java                  INTERFACE — void install(WebApplication app)

    Container.java               register / get; recursive constructor injection
    Registration.java            name, type, Lifecycle, provider
    Lifecycle.java               INTERFACE — Strategy; get(name, provider) : Object
    SingletonScope.java
    PrototypeScope.java
    HttpRequestScope.java

  webframework/plugin/xml/       (ideally a separate Gradle/Maven module)
    XmlMediaTypePlugin.java
    XmlBodySerializer.java

  app/
    Main.java                    the boot script
    controller/UserController.java
    service/UserService.java  TokenService.java
    repository/UserRepository.java     in-memory Map, sequential ids from 1
    domain/User.java  Token.java
    dto/RegisterRequest RegisterResponse LoginRequest LoginResponse
        RenameRequest UserView
    exception/DuplicateEmailException InvalidCredentialsException
              InvalidFormatException UnauthenticatedException ForbiddenException
```

### The dispatch pipeline (fixed, 8 steps)

Implement as a single well-factored method. Do **not** build a middleware chain yet — note in a
comment that this is the intended evolution point.

```
1. adapt raw HttpExchange → HttpRequest
2. open request context / request scope
3. match route
     no path match      → 404
     path but no method → 405
4. write path variables into the request
5. resolve handler instance from the Container
6. invoke handler
     throws → resolve exception rule → build error response, skip 7
7. serialize result
     has body → route's media type → serializer → 200
     no body  → 204, no content-type
8. write response; close request context in a finally
```

---

## 4. Member system API contract

> **Verify these exact strings against the original assignment PDF before shipping.** They are
> reconstructed and the grader may check them literally.

### A1 — 註冊 `POST /api/users`

Request `application/json`: `{ "email", "name", "password" }`

- `email` length 4–32 and valid email format
- `name` length 5–32
- `password` length 5–32
- invalid format → **400** `Registration's format incorrect.`
- email already used → **400** `Duplicate email`
- success → **201**, JSON body containing the new user's `id` (sequential, starting at 1)

### A2 — 登入 `POST /api/users/login`

Request `application/json`: `{ "email", "password" }`

- invalid format → **400** `Login's format incorrect.`
- wrong email or password → **400** `Credentials Invalid`
- success → **200**, JSON body containing a token: a unique random string **36–60 characters**
  (a UUID is acceptable)

### A3 — 改名 `PATCH /api/users/{userId}`

Header `Authorization: Bearer <token>`, request `application/json`: `{ "name" }`

- missing / unparseable / unknown token → **401** `Can't authenticate who you are.`
- valid token but `{userId}` is a different user → **403** `Forbidden`
- `name` length outside 5–32 → **400** `Name's format invalid.`
- success → **204**, **no body, no content-type header**

### A4 — 查詢會員 `GET /api/users?keyword=<optional>`

- no `keyword` → all users
- with `keyword` → users whose `name` contains it
- success → **200**, JSON array. Each entry exposes `id`, `email`, `name` — **never `password`**

### Global

- All error bodies are plain text with `content-type: text/plain`.
- Passwords are stored and compared in plaintext per the assignment. Add a `// KNOWN LIMITATION`
  comment; do not silently add hashing.

---

## 5. Milestones

Keep the app runnable after each. Do not start the next until the acceptance check passes.

### M1 — Walking skeleton
`WebApplication` boots the JDK server on a port; one hard-coded literal route returns a string.
Literal path matching only (`equals`), no variables. `JdkHttpRequest` adapter in place.

**Accept:** `curl -X POST localhost:8080/hello` returns 200 with the expected text, and no class
outside `JdkHttpRequest`/`JdkHttpServerAdapter` imports `com.sun.net.httpserver`.

### M2 — Real routing
`PathPattern` + `PathSegment` + `PathMatchResult`; `{userId}` extraction; query parameters; the
404 / 405 distinction with the exact body strings.

**Accept:** `GET /api/users/3` hits a `/api/users/{userId}` route with `userId=3`; an unknown path
returns the exact 404 text; a wrong method on a known path returns the exact 405 text.

### M3 — Serialization  ← *the member system becomes real here*
`BodySerializer`, `SerializerRegistry`, JSON + plain text. `readBodyAsObject`. Response serialization
by the route's configured media type, including the 204 no-body case.

**Accept:** A1–A4 all work end to end, including the 204 having no `content-type` header.

### M4 — Exception handling
`ExceptionRule`, `ExceptionRuleRegistry`, the default always-matching 500 rule. Replace M1's
hard-coded catch with a registry lookup. Register the app's own mappings in `Main`.

**Accept:** `InvalidCredentialsException` returns 400 with its message; an unmapped
`RuntimeException` returns 500 with its message; a DTO validation failure returns 400 (not 500) —
see §2.4.

### M5 — Plugins
`Plugin` interface; `app.addPlugin(...)` calls `plugin.install(app)`, and the plugin registers its own
serializer back into the registry. Move the XML plugin into its own module.

**Accept:** XML support works, and the framework module has zero references to any XML class.

### M6 — IoC container
`Container`, `Registration`, `Lifecycle`, `SingletonScope`, `PrototypeScope`, then
`HttpRequestScope` (needs the context open/close hooks from §2.6). Recursive constructor injection.
Rewire `Main` from `new` to `container.get(...)`.

**Accept:** a prototype-scoped dependency yields a different instance per `get()`; a request-scoped
dependency yields the same instance within one request and a different one across two requests; a
singleton is constructed lazily on first `get()`, once; the request scope is empty after a request
that threw.

### M7 — Polish
Boot script tidy, README with run instructions, a short note listing known limitations.

---

## 6. Tests worth writing (small, targeted)

Not exhaustive coverage — just the things most likely to be broken and hardest to eyeball:

- `PathPattern`: literal match, variable extraction, wrong segment count, trailing slash
- Router: the 404-vs-405 branch, and that path variables are written **once**, only on the winner
- `SerializerRegistry`: unknown media type → the 500 path
- `Container`: the three lifecycles' identity semantics; recursive constructor injection; lazy
  construction; scope cleanup after an exception
- End-to-end: one happy path and one error path per endpoint A1–A4
- 204: assert the response has **no** `content-type` header

---

## 7. What to flag rather than decide alone

Stop and ask if you hit any of these:

- an exact error string in §4 that appears to contradict the assignment PDF
- a place where honouring §2 seems to force genuinely ugly code (there may be a third option)
- whether `HttpRequestScope` should use a `ThreadLocal` or thread an explicit context — the design
  leans `ThreadLocal` to keep `Container` free of web-shaped parameters, but say so before committing
- anything that would require the framework to import from `app/`
