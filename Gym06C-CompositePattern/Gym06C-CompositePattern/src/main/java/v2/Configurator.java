package v2;
import com.fasterxml.jackson.databind.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configurator {
    public static void configure(String jsonFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));
        JsonNode loggersNode =  rootNode.get("loggers");

        //1. Build the root logger
        Level rootLevel = Level.valueOf(loggersNode.get("levelThreshold").asText());
        CompositeExporter rootExporter = parseExporter(loggersNode.get("exporter"));
        Layout rootLayout = parseLayout(loggersNode.get("layout").asText());

        Logger root = new Logger("root", rootLevel, null, rootLayout, rootExporter);
        LogManager.register("root", root);

        // 2. Recursively build child loggers
        parseChildren(loggersNode, root);
    }

    private static CompositeExporter parseExporter(JsonNode node){
        if(node == null){
            return null;
        }

        String type = node.get("type").asText();

        switch (type) {
            case "console":
                return new ConsoleExporter();
            case "file":
                String fileName = node.get("fileName").asText();
                return new FileExporter(fileName);
            case "composite":
                List<CompositeExporter> exporters = new ArrayList<>();
                JsonNode children = node.get("children");
                for (JsonNode child : children) {
                    exporters.add(parseExporter(child));
                }
                return new CompositeExporter(exporters);
            default:
                throw new IllegalArgumentException("Unknown exporter type: " + type);
        }
    }

    private static Layout parseLayout(String layout){
        switch (layout) {
            case "standard":
                return new StandardLayout();
            default:
                throw new IllegalArgumentException("Unknown layout type: " + layout);
        }
    }

    private static void parseChildren(JsonNode node, Logger parent) {

        node.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            // Ignore reserved configuration keys; everything else is a child logger
            if (key.equals("levelThreshold") || key.equals("exporter") || key.equals("layout") || key.equals("children")) {
                return;
            }

            // INHERITANCE LOGIC: If missing in JSON, take from parent
            Level level = value.has("levelThreshold")
                    ? Level.valueOf(value.get("levelThreshold").asText())
                    : parent.getThreshold(); // You'll need a getThreshold() in your Logger class

            CompositeExporter exporter = value.has("exporter")
                    ? parseExporter(value.get("exporter"))
                    : parent.getExporter(); // You'll need a getExporter() in your Logger class

            Layout layout = value.has("layout")
                    ? parseLayout(value.get("layout").asText())
                    : parent.getLayout(); // You'll need a getLayout() in your Logger class

            // Create and register this logger
            Logger currentLogger = new Logger(key, level, parent, layout, exporter);
            LogManager.register(key, currentLogger);

            // Recursion: Check if THIS logger has children nested inside it (like app.game.ai)
            parseChildren(value, currentLogger);
        });
    }
}
