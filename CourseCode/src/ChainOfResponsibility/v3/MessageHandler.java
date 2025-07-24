package ChainOfResponsibility.v3;

public abstract class MessageHandler {
    protected MessageHandler next;

    public MessageHandler(MessageHandler next) {
        this.next = next;
    }

    public boolean handle(String message){
        if(match(message)) {
            return doHandling(message);
        } else if (next != null) {
            return next.handle(message);
        } else {
            System.out.println("Wrong !");
            return false;
        }
    }

    public abstract boolean match(String message);

    public abstract boolean doHandling(String message);
}
