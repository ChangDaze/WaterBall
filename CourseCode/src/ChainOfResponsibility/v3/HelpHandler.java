package ChainOfResponsibility.v3;

public class HelpHandler extends MessageHandler {
    public HelpHandler(MessageHandler next) {
        super(next);
    }

    @Override
    public boolean match(String message) {
        return message.equalsIgnoreCase("help");
    }

    @Override
    public boolean doHandling(String message) {
        System.out.println("I have no idea!");
        return false;
    }
}
