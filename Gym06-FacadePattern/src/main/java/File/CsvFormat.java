package File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Builder
public class CsvFormat{
    private final ObjectMapper mapper = new ObjectMapper();

    public <T> void write(Path path, List<T> data) {
        try{
            JsonNode array = mapper.valueToTree(data);
            StringBuilder sb = new StringBuilder();

            for (JsonNode obj : array) {
                obj.fields().forEachRemaining(e -> {
                    JsonNode value = e.getValue();
                    if (value.isContainerNode()) { // List 或 Object
                        try {
                            sb.append(mapper.writeValueAsString(value));
                        } catch (Exception ex) {
                            sb.append("\"\"");
                        }
                    } else {
                        sb.append(value.asText());
                    }
                    sb.append(",");
                });
                sb.setLength(sb.length() - 1);
                sb.append("\n");
            }

            Files.writeString(path, sb.toString());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
