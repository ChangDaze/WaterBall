package v5.domain;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private final String id;
    private  final String title;
    private String content;
    private final String[] tags;
    private final Member author;
    private final List<Comment> comments = new ArrayList<>();

    public Post(String id, String title, String content, String[] tags, Member author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.author = author;
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public String[] tags() {
        return tags;
    }

    public Member author() {
        return author;
    }

    void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Comment> comments() {
        return comments;
    }
}
