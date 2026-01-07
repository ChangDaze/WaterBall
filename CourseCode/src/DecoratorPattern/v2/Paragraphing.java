package DecoratorPattern.v2;

import DecoratorPattern.commons.Message;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class Paragraphing extends MessageProcessor{
    public Paragraphing(Messenger next){super(next);}

    @Override
    public void send(List<Message> messages) {
        messages = messages
                .stream()
                .flatMap(msg -> paragraph(msg).stream())
                .collect(Collectors.toUnmodifiableList());

        next.send(messages);
    }

    private List<Message> paragraph(Message message) {
        return stream(message.getContent().split("\n\n")).map(message::edit).collect(toList());
    }
}
