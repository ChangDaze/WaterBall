package app.domain;

/**
 * A member. Its id is assigned by the repository and never changes; the name can.
 */
public final class User {

    private final int id;
    private final String email;
    private String name;

    // KNOWN LIMITATION: the assignment specifies plaintext storage and comparison, so no hashing is
    // applied here. A real system would store a salted hash and compare in constant time.
    private final String password;

    public User(int id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void rename(String name) {
        this.name = name;
    }

    public boolean hasPassword(String candidate) {
        // KNOWN LIMITATION: plaintext comparison, per the assignment.
        return password.equals(candidate);
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
