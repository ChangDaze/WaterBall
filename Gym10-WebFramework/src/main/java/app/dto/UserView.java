package app.dto;

import app.domain.User;

/**
 * A4 success body element: what a member looks like to the outside world.
 *
 * <p>This class is the reason A4 cannot leak a password — the domain {@link User} is never serialized
 * directly, so a field added to it later cannot silently appear on the wire.
 */
public final class UserView {

    private int id;
    private String email;
    private String name;

    public UserView() {
    }

    public UserView(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserView of(User user) {
        return new UserView(user.getId(), user.getEmail(), user.getName());
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
