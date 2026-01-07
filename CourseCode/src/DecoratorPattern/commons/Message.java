package DecoratorPattern.commons;

import java.time.LocalDateTime;

public class Message {
    private final User from;
    private final User to;
    private final String content;
    private final LocalDateTime createdTime;

    public Message(User from, User to, String content) {
        this(from, to, content, LocalDateTime.now());
    }

    public Message(User from, User to, String content, LocalDateTime createdTime) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.createdTime = createdTime;
    }

    public LocalDateTime getCreatedTime(){return createdTime;}

    public String getContent(){return content;}

    public User getFrom(){return from;}

    public String getFromName(){return from.getNickname();}

    public User getTo(){return to;}

    public String getToName(){return to.getNickname();}

    public Message edit(String content){return new Message(from, to, content, createdTime);}
}
