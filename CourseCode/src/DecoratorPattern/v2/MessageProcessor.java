package DecoratorPattern.v2;

public abstract class MessageProcessor implements Messenger{
    protected Messenger next;
    public MessageProcessor(Messenger next) { this.next = next;}
}
