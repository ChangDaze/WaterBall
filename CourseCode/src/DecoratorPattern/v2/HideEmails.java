package DecoratorPattern.v2;

import DecoratorPattern.commons.Message;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HideEmails extends MessageProcessor{
    public HideEmails(Messenger next){super(next);}

    @Override
    public void send(List<Message> messages) {
        messages = messages
                .stream()
                .map(this::hideEmails)
                .collect(Collectors.toUnmodifiableList());

        next.send(messages);
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
}
