package v2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileExporter extends CompositeExporter {
    private final String fileName;
    private final Path filePath;

    public FileExporter(String fileName){
        this.fileName = fileName;
        this.filePath = Paths.get(fileName);
    }

    @Override
    public void export(String message) {
        try {
            Files.write(
                    filePath,
                    message.getBytes(),
                    StandardOpenOption.CREATE,  // Create file if it doesn't exist
                    StandardOpenOption.APPEND  // Append to the end of the file
            );
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
