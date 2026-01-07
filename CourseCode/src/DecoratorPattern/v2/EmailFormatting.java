package DecoratorPattern.v2;

import DecoratorPattern.commons.Message;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class EmailFormatting extends MessageProcessor{
    public EmailFormatting(Messenger next){super(next);}

    @Override
    public void send(List<Message> messages) {
        messages = messages
                .stream()
                .map(this::mailFormatting)
                .collect(Collectors.toUnmodifiableList());

        next.send(messages);
    }

    private Message mailFormatting(Message message){
        return message.edit(format("Dear %s, \n\n%s\n\nBest regards,\n %s", message.getToName(), message.getContent(), message.getFromName()));
    }
}
