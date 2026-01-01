package File;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;

import java.nio.file.Path;
import java.util.List;

@Builder
public class JsonFormat{
    private static final ObjectMapper mapper = new ObjectMapper();

    public <T> List<T> read(Path path, Class<T> clazz) {
        try {
            return mapper.readValue(
                    path.toFile(),
                    mapper.getTypeFactory()
                            .constructCollectionType(List.class, clazz)
            );
        }catch (Exception e){
            throw new RuntimeException("JSON read failed: " + path, e);
        }
    }

    public <T> void write(Path path, List<T> data) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), data);
        }catch (Exception e){
            throw new RuntimeException("JSON write failed: " + path, e);
        }
    }
}
