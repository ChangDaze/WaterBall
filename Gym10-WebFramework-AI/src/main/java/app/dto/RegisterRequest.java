package app.dto;

import app.exception.InvalidFormatException;
import webframework.serialization.Validatable;

/**
 * A1 request body: {@code { "email", "name", "password" }}.
 *
 * <p>The no-argument constructor is what the deserializers build; the full one is for tests and for
 * hand-assembling a request in code.
 */
public final class RegisterRequest implements Validatable {

    private String email;
    private String name;
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * A1: email 4–32 and a valid address, name 5–32, password 5–32.
     *
     * <p>One message for every kind of malformed input, as the contract specifies — the caller is not
     * told which field offended.
     */
    @Override
    public void validate() {
        boolean valid = Formats.isLengthBetween(email, 4, 32)
                && Formats.isEmail(email)
                && Formats.isLengthBetween(name, 5, 32)
                && Formats.isLengthBetween(password, 5, 32);
        if (!valid) {
            throw new InvalidFormatException("Registration's format incorrect.");
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
