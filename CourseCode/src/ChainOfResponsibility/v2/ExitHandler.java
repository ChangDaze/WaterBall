package ChainOfResponsibility.v2;

public class ExitHandler extends MessageHandler{
    public ExitHandler(MessageHandler next) {
        super(next);
    }

    @Override
    public boolean handle(String message) {
        if (message.equalsIgnoreCase("exit")){
            System.out.println("Bye~");
            return true;
        } else if (next != null) {
            return next.handle(message);
        }
        return false;
    }
}
