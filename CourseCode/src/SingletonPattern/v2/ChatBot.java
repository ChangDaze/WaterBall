package SingletonPattern.v2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChatBot {

    private final static ChatBot instance = new ChatBot(); //靜態共用單體


    public static ChatBot getInstance() {
        //單體存取的共用靜態方法
        return instance;
    }

    private ChatBot() {
        //禁止外部存取
    }

    public String chatGetArrayOfStrings(String limit) throws Exception {
        checkThrottling();
        return "ChatBot: " + getArrayOfStrings(limit);
    }

    private String getArrayOfStrings(String limit) throws Exception{
        String apiUrl = String.format("https://apiservice.mol.gov.tw/OdService/rest/group?limit=%s", limit);
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue( new URL(apiUrl) , String[].class);
        return Arrays.toString(result);
    }

    public String chatPostString(String title) throws Exception {
        checkThrottling();
        return "ChatBot: " + postString(title);
    }

    public String postString(String title) throws Exception{
        /*
        * https://ithelp.ithome.com.tw/articles/10301744
        * https://jsonplaceholder.typicode.com/
        * 現在jsonplaceholder post只會丟回id 101 => 跟文章上有點不同，所以再去get 101 => 但會404所以最後再 get 1
        * */
        String apiUrl = "https://jsonplaceholder.typicode.com/posts";

        // Create a map for the request body
        Map<String, Object> postData = new HashMap<>();
        postData.put("title", title);
        postData.put("body", "test body");
        postData.put("userId", 1);

        // Send POST request
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(postData);
        System.out.println("Request Body: " + jsonBody);

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        try(OutputStream os = connection.getOutputStream()) {
            os.write(jsonBody.getBytes("utf-8"));
        }

        // Check response code
        int responseCode = connection.getResponseCode();
        System.out.println("Post Response Code: " + responseCode);

        // Deserialize the response as Map instead of String to avoid JSON parsing error
        Map<String, Object> responseMap = mapper.readValue(connection.getInputStream(), Map.class);
        System.out.println("Post Response Object: " + mapper.writeValueAsString(responseMap));
        System.out.println("Try Get Get Response");
        return getFullPost(responseMap.get("id").toString());
    }

    private String getFullPost(String id) throws Exception {
        String getUrl = "https://jsonplaceholder.typicode.com/posts/" + id;
        HttpURLConnection connection = (HttpURLConnection) new URL(getUrl).openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            System.out.println("Server Verification Failed: Received HTTP " + responseCode +
                    " (This is expected on JSONPlaceholder for ID 101!)");
            return getFullPost("1"); // Fallback to a valid ID to demonstrate GET request
        }
    }

    private void checkThrottling() throws Exception {
        String filePath = "Throttling.txt";

        // Read current value from file
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        reader.close();

        int currentValue = Integer.parseInt(line.trim());
        System.out.println("Current Throttling Value: " + currentValue);

        //Check if value is greater than 0
        if(currentValue <= 0) {
            throw new Exception("Throttling limit reached. Please try again later.");
        }

        // Decrement value by 1
        int newValue = currentValue - 1;

        // Write new value back to file
        FileWriter writer = new FileWriter(filePath);
        writer.write(String.valueOf(newValue));
        writer.close();

        System.out.println("Throttling Value Updated: " + newValue);
    }
}
