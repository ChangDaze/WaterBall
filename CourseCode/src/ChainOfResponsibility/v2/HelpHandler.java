package ChainOfResponsibility.v2;

public class HelpHandler extends MessageHandler{
    public HelpHandler(MessageHandler next) {
        super(next);
    }

    @Override
    public boolean handle(String message) {
        if (message.equalsIgnoreCase("help")){
            System.out.println("I have no idea!");
        } else if (next != null) {
            return next.handle(message);
        }

        return false;
    }
}
