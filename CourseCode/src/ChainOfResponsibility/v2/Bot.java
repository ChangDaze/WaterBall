package ChainOfResponsibility.v2;

public class Bot {

    private MessageHandler handler;

    public Bot(MessageHandler handler) {
        this.handler = handler;
    }

    public boolean handle(String message) {
        if (message.equalsIgnoreCase("help")){
            System.out.println("I have no idea!");
        } else if (message.equalsIgnoreCase("answer me")) {
            System.out.println("Bot is here!");
        }
        else if (message.equalsIgnoreCase("exit")) {
            System.out.println("Bye~");
            return  true;
        } else {
            System.out.println("Wrong !");
        }
        return false;
    }
}
