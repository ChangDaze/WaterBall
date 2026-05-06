package SingletonPattern.v2;

public class Main {
    public static void main(String[] args) throws Exception {
        ChatBot chatBot1 = ChatBot.getInstance(); //透過共用靜態方法存取單體
        ChatBot chatBot2 = ChatBot.getInstance(); //透過共用靜態方法存取單體

        System.out.println(chatBot1.chatGetArrayOfStrings("5"));
        System.out.println(chatBot2.chatGetArrayOfStrings("10"));

        System.out.println(chatBot1.postString("chatBot1"));
        System.out.println(chatBot2.postString("chatBot2"));

        //同個單幾
        System.out.println("chatBot1 " + chatBot1);
        System.out.println("chatBot2 " + chatBot2);
    }
}
