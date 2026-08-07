package app.service;

import java.util.List;

import app.domain.Token;
import app.domain.User;
import app.dto.LoginRequest;
import app.dto.LoginResponse;
import app.dto.RegisterRequest;
import app.dto.RegisterResponse;
import app.dto.UserView;
import app.exception.DuplicateEmailException;
import app.exception.ForbiddenException;
import app.exception.InvalidCredentialsException;
import app.exception.UnauthenticatedException;
import app.repository.UserRepository;

/**
 * The member system's use cases. Knows nothing about HTTP: no status codes, no headers, no request
 * object — it throws exceptions and lets the framework's rules decide what those mean on the wire.
 */
public final class UserService {

    private final UserRepository users;
    private final TokenService tokens;

    public UserService(UserRepository users, TokenService tokens) {
        this.users = users;
        this.tokens = tokens;
    }

    /**
     * A1. The body has already validated its own format by the time it arrives here.
     */
    public RegisterResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Duplicate email");
        }
        User user = users.save(request.getEmail(), request.getName(), request.getPassword());
        return new RegisterResponse(user.getId());
    }

    /**
     * A2.
     */
    public LoginResponse login(LoginRequest request) {
        User user = users.findByEmail(request.getEmail())
                .filter(candidate -> candidate.hasPassword(request.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Credentials Invalid"));
        return new LoginResponse(tokens.issue(user).getValue());
    }

    /**
     * A3, first half: who is calling.
     *
     * @param tokenValue the token taken from the {@code Authorization} header, or {@code null} if the
     *                   header was missing or not a bearer token
     * @return the authenticated member's id
     * @throws UnauthenticatedException for a missing, unparseable or unknown token
     */
    public int authenticate(String tokenValue) {
        Token token = tokens.resolve(tokenValue)
                .orElseThrow(() -> new UnauthenticatedException("Can't authenticate who you are."));
        return token.getUserId();
    }

    /**
     * A3, second half: whether they may act on this member.
     *
     * <p>Separate from {@link #rename} so the controller can run this check before reading the
     * request body — the contract puts 403 ahead of the body's own 400.
     *
     * @throws ForbiddenException if the caller is acting on someone else
     */
    public void checkCanRename(int actorUserId, int targetUserId) {
        if (actorUserId != targetUserId) {
            throw new ForbiddenException("Forbidden");
        }
    }

    /**
     * A3, third half: do it.
     */
    public void rename(int userId, String name) {
        User user = users.findById(userId)
                .orElseThrow(() -> new ForbiddenException("Forbidden"));
        user.rename(name);
    }

    /**
     * A4.
     *
     * @param keyword {@code null} or blank for every member, otherwise a name substring
     */
    public List<UserView> query(String keyword) {
        List<User> found = keyword == null || keyword.isEmpty()
                ? users.findAll()
                : users.findByNameContaining(keyword);
        return found.stream().map(UserView::of).toList();
    }
}
