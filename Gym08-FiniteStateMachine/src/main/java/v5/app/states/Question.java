package v5.app.states;

/**
 * question object, current extend the ability to render question to human-readable.
 * record class can also provide the function to be called.
 */
public record Question (String text, String optionA, String optionB, String optionC, String optionD, String correctOption){
    public String render(int number){
        return number + ". " + text
                + "\nA) " + optionA
                + "\nB) " + optionB
                + "\nC) " + optionC
                + "\nD) " + optionD;
    }
}
