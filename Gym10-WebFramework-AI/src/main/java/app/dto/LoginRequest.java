package app.dto;

import app.exception.InvalidFormatException;
import webframework.serialization.Validatable;

/**
 * A2 request body: {@code { "email", "password" }}.
 */
public final class LoginRequest implements Validatable {

    private String email;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * A2: the same field rules as registration — anything registration would have rejected cannot
     * belong to an existing member, so it is a format error rather than a credentials error.
     */
    @Override
    public void validate() {
        boolean valid = Formats.isLengthBetween(email, 4, 32)
                && Formats.isEmail(email)
                && Formats.isLengthBetween(password, 5, 32);
        if (!valid) {
            throw new InvalidFormatException("Login's format incorrect.");
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
