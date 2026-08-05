package app.dto;

/**
 * A1 success body: the new member's id.
 */
public final class RegisterResponse {

    private int id;

    public RegisterResponse() {
    }

    public RegisterResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
