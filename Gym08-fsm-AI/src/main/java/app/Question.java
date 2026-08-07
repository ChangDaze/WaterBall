package app;

public record Question(String text, String optionA, String optionB, String optionC, String optionD,
                       String correctOption) {

    /** Render as "<i>. <text>\nA) ...\nB) ...\nC) ...\nD) ..." (i is 0-based, per the .out files). */
    public String render(int number) {
        return number + ". " + text
                + "\nA) " + optionA
                + "\nB) " + optionB
                + "\nC) " + optionC
                + "\nD) " + optionD;
    }
}
