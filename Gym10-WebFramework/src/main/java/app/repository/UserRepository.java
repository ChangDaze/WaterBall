package app.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import app.domain.User;

/**
 * In-memory member storage. Ids are sequential from 1.
 *
 * <p>Sorted by id so {@code findAll} returns members in registration order. Everything lives in the
 * process — restarting the application starts over from an empty store.
 */
public final class UserRepository {

    private final Map<Integer, User> usersById = new ConcurrentSkipListMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * Creates and stores a member, assigning the next id.
     */
    public User save(String email, String name, String password) {
        User user = new User(nextId.getAndIncrement(), email, name, password);
        usersById.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return usersById.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public List<User> findAll() {
        return new ArrayList<>(usersById.values());
    }

    public List<User> findByNameContaining(String keyword) {
        return usersById.values().stream()
                .filter(user -> user.getName().contains(keyword))
                .toList();
    }
}
