package DecoratorPattern.v1;

import DecoratorPattern.commons.Message;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class EmailFormattingAndParagraphingMessenger extends Messenger{
    @Override
    public void sendMessage(Message message) {
        message = mailFormatting(message);
        List<Message> messages = paragraph(message);
        printMessage(messages);
    }

    private Message mailFormatting(Message message){
        return message.edit(format("Dear %s, \n\n%s\n\nBest regards,\n %s", message.getToName(), message.getContent(), message.getFromName()));
    }

    private List<Message> paragraph(Message message) {
        return stream(message.getContent().split("\n\n")).map(message::edit).collect(toList());
    }

    private void printMessage(List<Message> messages) {
        for(Message message: messages) {
            System.out.printf("[%s] %s: \n%s\n",
                    message.getCreatedTime().format(DateTimeFormatter.ofPattern("MM/dd hh:mm")),
                    message.getFrom(),
                    message.getContent());
        }
    }
}
