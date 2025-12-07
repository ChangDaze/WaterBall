package FacadePattern.v1;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readString;
import static java.util.Arrays.asList;

public class MarkdownParser {
    public Table parseTableFromFile(String fileName) throws IOException {
        String markdown = readString(Paths.get(fileName));
        String[] lines = markdown.split("\n");
        List<List<String>> fields = new ArrayList<>();
        fields.add(parseRow(0, lines)); //取得header
        //markdown第二列是分割header和value的分隔線
        for(int i = 2; i < lines.length; i++) { //解析value
            fields.add(parseRow(i, lines));
        }
        return new Table(fields);
    }

    private List<String> parseRow(int i, String[] lines) {
        return asList(
                lines[i]
                    .replaceFirst("[|\\s]+","") // 移除第一個| +空白
                    .split("[|\\s]+")//再用| +空白分割
                );
    }
}
