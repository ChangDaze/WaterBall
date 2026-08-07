package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import domain.Community;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * One dispatch table for the input boundary: a name → handler map translating
 * each input line into a typed Community call (method-call style — no
 * input-event object family). Unknown names / malformed JSON throw loudly.
 */
public class InputDispatcher {
    private static final Pattern ELAPSED = Pattern.compile("^\\[(\\d+) (seconds|minutes|hours) elapsed\\]$");
    private static final Pattern GENERAL = Pattern.compile("^\\[(.+?)\\](?: (\\{.*\\}))?$");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Community community;
    private final Map<String, Consumer<JsonObject>> handlers = new HashMap<>();

    public InputDispatcher(Community community) {
        this.community = community;
        handlers.put("started", json ->
                community.start(parseTime(json.get("time").getAsString()), json.get("quota").getAsInt()));
        handlers.put("login", json ->
                community.login(json.get("userId").getAsString(), bool(json, "isAdmin")));
        handlers.put("logout", json ->
                community.logout(json.get("userId").getAsString()));
        handlers.put("new message", json ->
                community.sendMessage(json.get("authorId").getAsString(),
                        json.get("content").getAsString(), tags(json)));
        handlers.put("new post", json ->
                community.newPost(json.get("authorId").getAsString(), json.get("id").getAsString(),
                        json.get("title").getAsString(), json.get("content").getAsString(), tags(json)));
        handlers.put("comment", json ->
                community.addComment(json.get("postId").getAsString(),
                        json.get("authorId").getAsString(), json.get("content").getAsString()));
        handlers.put("go broadcasting", json ->
                community.goBroadcasting(json.get("speakerId").getAsString()));
        handlers.put("speak", json ->
                community.speak(json.get("speakerId").getAsString(), json.get("content").getAsString()));
        handlers.put("stop broadcasting", json ->
                community.stopBroadcasting(json.get("speakerId").getAsString()));
    }

    public void dispatch(String line) {
        Matcher elapsed = ELAPSED.matcher(line);
        if (elapsed.matches()) {
            community.elapse(Integer.parseInt(elapsed.group(1)), elapsed.group(2));
            return;
        }
        Matcher general = GENERAL.matcher(line);
        if (!general.matches()) {
            throw new IllegalArgumentException("Unrecognized input line: " + line);
        }
        String name = general.group(1);
        Consumer<JsonObject> handler = handlers.get(name);
        if (handler == null) {
            throw new IllegalArgumentException("Unknown command: " + name);
        }
        String payload = general.group(2);
        JsonObject json = payload == null
                ? new JsonObject()
                : JsonParser.parseString(payload).getAsJsonObject();
        handler.accept(json);
    }

    private static LocalDateTime parseTime(String raw) {
        return raw.contains("T")
                ? LocalDateTime.parse(raw)
                : LocalDateTime.parse(raw, TIME_FORMAT);
    }

    private static boolean bool(JsonObject json, String field) {
        return json.has(field) && json.get(field).getAsBoolean();
    }

    private static String[] tags(JsonObject json) {
        if (!json.has("tags")) {
            return new String[0];
        }
        JsonArray array = json.getAsJsonArray("tags");
        String[] tags = new String[array.size()];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = array.get(i).getAsString();
        }
        return tags;
    }
}
