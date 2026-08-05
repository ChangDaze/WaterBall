package app.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import app.domain.Token;
import app.domain.User;

/**
 * Issues and resolves bearer tokens.
 *
 * <p>A2 requires a unique random string of 36–60 characters; a UUID is 36 and is explicitly
 * acceptable.
 */
public final class TokenService {

    private final Map<String, Token> tokensByValue = new ConcurrentHashMap<>();

    public Token issue(User user) {
        Token token = new Token(UUID.randomUUID().toString(), user.getId());
        tokensByValue.put(token.getValue(), token);
        return token;
    }

    /**
     * @return the token, or empty if the value was never issued
     */
    public Optional<Token> resolve(String value) {
        if (value == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(tokensByValue.get(value));
    }
}
