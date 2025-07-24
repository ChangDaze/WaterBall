package ChainOfResponsibility.v3;

public class AnswerMeHandler extends MessageHandler {
    public AnswerMeHandler(MessageHandler next) {
        super(next);
    }

    @Override
    public boolean match(String message) {
        return message.equalsIgnoreCase("answer me");
    }

    @Override
    public boolean doHandling(String message) {
        System.out.println("Bot is here!");
        return false;
    }
}
