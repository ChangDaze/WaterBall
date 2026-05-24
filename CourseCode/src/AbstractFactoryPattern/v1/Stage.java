package AbstractFactoryPattern.v1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

//Stage是專門處理答題的物件，Region是介於Floor和Stage間溝通
public class Stage {
    private final Floor floor; //Stage一次會問3個問題，跟Region一樣都歸屬Floor底下
    private final List<Question> questions;
    private int fail = 0;

    public Stage(Floor floor){
        this.floor = floor;
        try {
            this.questions = loadQuestions();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Question> loadQuestions() throws IOException {
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
    }

    private Question parseQuestion(String questionRaw){
        String[] parts = questionRaw.split("\\s*---\\s*");
        return new Question(parts[0], parts[1].trim());
    }

    public void play(Player player) {
        for(int i = 0; i < questions.size(); i++){
            Question question = questions.get(i);
            question.ask(i+1);
            String answer = player.answer();
            if(question.isCorrectAnswer(answer)){
                System.out.println("You are correct!");
            }else{
                System.out.println("You are wrong!");
                if(++fail >= 2){
                    System.out.println("You lose the game!");
                    player.loseGame();
                    return;
                }
            }
        }
    }
}
