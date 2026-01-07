package DecoratorPattern.v1;

import DecoratorPattern.commons.Message;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class EmailHidingAndParagraphingMessenger extends Messenger{
    @Override
    public void sendMessage(Message message) {
        message = hideEmails(message);
        List<Message> messages = paragraph(message);
        printMessage(messages);
    }

    private Message hideEmails(Message message){
        return message.edit(
                Pattern.compile("[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}")//Regex 編譯成正則物件 (找email)
                        .matcher(message.getContent()) //尋找所有符合 regex 的子字串
                        .replaceAll(res -> hideEmail(res.group())) //被比對到的內容做hideEmail後replace
        );
    }

    private String hideEmail(String email) {
        String[] parts = email.split("@");//分前後，只hide前面
        char[] chars = parts[0].toCharArray();
        int hiddenChars = (int) Math.ceil(chars.length/ 5f);
        for(int i = 0; i < hiddenChars; i++){
            chars[hiddenChars + i] = '*';//藏住中間幾個字
        }
        return String.format("%s@%s", new String(chars), parts[1]);
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
