package AbstractFactoryPattern.v3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

//Stage是專門處理答題的物件，Region是介於Floor和Stage間溝通
public abstract class Stage {
    protected int fail;

    //差異流程
    protected abstract List<Question> loadQuestions();

    public boolean failed() {
        return fail >= 2;
    }

    public void play(Player player) {
        List<Question> questions = loadQuestions();
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
