package DecoratorPattern.v1;

import DecoratorPattern.commons.Email;
import DecoratorPattern.commons.Message;
import DecoratorPattern.commons.User;

public class Main {
    public static void main(String[] args) {
        User waterball = new User(new Email("johnny","waterballsa.tw"), "水球");
        User tsen = new User(new Email("tsen", "waterballsa.tw"), "岑岑");
        Message message = new Message(waterball, tsen,
                "Could you tell the following people (wally@waterballsa.tw, fixilabis@waterballsa.tw, fong-putao@waterballsa.tw)\n\nthat I'm gonna take a leave today? Thanks!");

        System.out.println("====== EmailFormattingAndParagraphingMessenger ======");
        new EmailFormattingAndParagraphingMessenger().sendMessage(message);
        System.out.println("====== EmailHidingAndParagraphingMessenger ======");
        new EmailHidingAndParagraphingMessenger().sendMessage(message);
    }
}
