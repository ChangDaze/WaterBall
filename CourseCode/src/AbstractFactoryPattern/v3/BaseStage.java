package AbstractFactoryPattern.v3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

//Stage是專門處理答題的物件，Region是介於Floor和Stage間溝通
public class BaseStage extends Stage  {
    private final Floor floor; //Stage一次會問3個問題，跟Region一樣都歸屬Floor底下

    public BaseStage(Region region) {
        this.floor = region.getFloor();
    }

    @Override
    protected List<Question> loadQuestions(){
        try
        {
            // Canonical 典範
            String floorCanonicalName = floor.getName().toLowerCase().replace(" ","");
            //讀檔x.questions讀入問題
            String raw = Files.readString(Paths.get(format("%s.questions", floorCanonicalName)));
            String[] questionRaws = raw.split("===");
            //隨機取3個題目
            return stream(questionRaws)
                    .map(this::parseQuestion)
                    .sorted((a,b) -> (int) (Math.random() * 3 -1))
                    .limit(3)
                    .collect(toList());
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    private Question parseQuestion(String questionRaw){
        String[] parts = questionRaw.split("\\s*---\\s*");
        String description = parts[0].trim();
        String answer = parts[1].trim();
        return new Question(description, answer);
    }
}
