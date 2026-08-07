package domain;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private final List<Message> messages = new ArrayList<>();

    public void add(Message message) {
        messages.add(message);
    }

    public List<Message> messages() {
        return messages;
    }
}
