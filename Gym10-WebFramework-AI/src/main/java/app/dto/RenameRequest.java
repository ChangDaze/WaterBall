package app.dto;

import app.exception.InvalidFormatException;
import webframework.serialization.Validatable;

/**
 * A3 request body: {@code { "name" }}.
 */
public final class RenameRequest implements Validatable {

    private String name;

    public RenameRequest() {
    }

    public RenameRequest(String name) {
        this.name = name;
    }

    /**
     * A3: name 5–32 characters.
     */
    @Override
    public void validate() {
        if (!Formats.isLengthBetween(name, 5, 32)) {
            throw new InvalidFormatException("Name's format invalid.");
        }
    }

    public String getName() {
        return name;
    }
}
