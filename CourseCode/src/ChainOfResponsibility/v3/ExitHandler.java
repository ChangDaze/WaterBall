package ChainOfResponsibility.v3;

public class ExitHandler extends MessageHandler {
    public ExitHandler(MessageHandler next) {
        super(next);
    }

    @Override
    public boolean match(String message) {
        return message.equalsIgnoreCase("exit");
    }

    @Override
    public boolean doHandling(String message) {
        System.out.println("Bye~");
        return true;
    }
}
