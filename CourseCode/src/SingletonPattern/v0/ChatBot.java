package SingletonPattern.v0;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Arrays;

public class ChatBot {
    public String chatgetArrayOfStrings(String limit) throws Exception {
        return "ChatBot: " + getArrayOfStrings(limit);
    }

    private String getArrayOfStrings(String limit) throws Exception{
        String apiUrl = String.format("https://apiservice.mol.gov.tw/OdService/rest/group?limit=%s", limit);
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue( new URL(apiUrl) , String[].class);
        return Arrays.toString(result);
    }

    public static void main(String[] args) throws Exception {
        ChatBot chatBot1 = new ChatBot();
        ChatBot chatBot2 = new ChatBot();

        System.out.println(chatBot1.chatgetArrayOfStrings("5"));
        System.out.println(chatBot2.chatgetArrayOfStrings("10"));

        System.out.println("chatBot1 " + chatBot1);
        System.out.println("chatBot1 " + chatBot2);
    }
}
