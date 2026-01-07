package DecoratorPattern.v2;

import DecoratorPattern.commons.Message;

import java.util.List;

import static java.util.Collections.singletonList;

public interface Messenger {
    default void send(Message message) {send(singletonList(message));} //傳單一Message進來直接轉List
    void send(List<Message> messages);
}
