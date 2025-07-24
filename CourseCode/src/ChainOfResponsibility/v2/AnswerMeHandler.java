package ChainOfResponsibility.v2;

public class AnswerMeHandler extends MessageHandler{
    public AnswerMeHandler(MessageHandler next) {
        super(next);
    }

    @Override
    public boolean handle(String message) {
        if (message.equalsIgnoreCase("answer me")){
            System.out.println("Bot is here!");
        } else if (next != null) {
            return next.handle(message);
        }

        return false;
    }
}
