package domain;

import java.util.ArrayList;
import java.util.List;

public class Forum {
    private final List<Post> posts = new ArrayList<>();

    void add(Post post) {
        posts.add(post);
    }

    public Post findPost(String id) {
        return posts.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No post with id " + id));
    }

    public List<Post> posts() {
        return posts;
    }
}
