package ChainOfResponsibility.v2;

public class TailHandler extends MessageHandler{
    public TailHandler(MessageHandler next) {
        super(next);
    }

    @Override
    public boolean handle(String message) {
        System.out.println("Wrong !");
        return false;
    }
}
