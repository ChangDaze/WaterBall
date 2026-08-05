package app.controller;

import java.util.List;

import app.dto.LoginRequest;
import app.dto.LoginResponse;
import app.dto.RegisterRequest;
import app.dto.RegisterResponse;
import app.dto.RenameRequest;
import app.dto.UserView;
import app.exception.ForbiddenException;
import app.service.UserService;
import webframework.http.HttpRequest;

/**
 * The member system's HTTP surface: A1–A4.
 *
 * <p>Notice what is absent. No path is switched on, no status code is set, no serializer is chosen,
 * no error body is written. A handler returns the object to serialize — or returns nothing, which the
 * framework turns into a 204 — and throws when it cannot.
 */
public final class UserController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * A1 {@code POST /api/users} → 201 with the new id.
     */
    public RegisterResponse register(HttpRequest request) {
        return userService.register(request.readBodyAsObject(RegisterRequest.class));
    }

    /**
     * A2 {@code POST /api/users/login} → 200 with a token.
     */
    public LoginResponse login(HttpRequest request) {
        return userService.login(request.readBodyAsObject(LoginRequest.class));
    }

    /**
     * A3 {@code PATCH /api/users/{userId}} → 204, no body.
     *
     * <p>The order of these four lines is the contract: 401 before 403, and 403 before the body's own
     * 400. Reading the body earlier would validate a name for a caller who was never allowed to
     * change it, and report the wrong status.
     */
    public void rename(HttpRequest request) {
        int actorUserId = userService.authenticate(bearerTokenOf(request));
        int targetUserId = targetUserIdOf(request);
        userService.checkCanRename(actorUserId, targetUserId);
        RenameRequest body = request.readBodyAsObject(RenameRequest.class);
        userService.rename(targetUserId, body.getName());
    }

    /**
     * A4 {@code GET /api/users?keyword=} → 200 with an array of members.
     */
    public List<UserView> queryUsers(HttpRequest request) {
        return userService.query(request.getQueryParameter("keyword"));
    }

    /**
     * @return the token from {@code Authorization: Bearer <token>}, or {@code null} if the header is
     *         missing or shaped differently — either way the service reports it as unauthenticated
     */
    private static String bearerTokenOf(HttpRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }
        String token = authorization.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? null : token;
    }

    /**
     * A non-numeric {@code {userId}} is treated as "some other member": the caller is authenticated,
     * so the honest answer is that they may not touch it.
     */
    private static int targetUserIdOf(HttpRequest request) {
        try {
            return Integer.parseInt(request.getPathVariable("userId"));
        } catch (NumberFormatException e) {
            throw new ForbiddenException("Forbidden");
        }
    }
}
