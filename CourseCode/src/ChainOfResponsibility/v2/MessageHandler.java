package ChainOfResponsibility.v2;

public abstract class MessageHandler {
    protected MessageHandler next;

    public MessageHandler(MessageHandler next) {
        this.next = next;
    }

    public abstract boolean handle(String message);
}
