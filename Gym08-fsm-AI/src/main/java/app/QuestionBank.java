package app;

import java.util.List;

/**
 * The three fixed KnowledgeKing questions — strings are byte-for-byte from
 * the ground-truth .out files (numbered from 0).
 */
public class QuestionBank {
    private final List<Question> questions = List.of(
            new Question("請問哪個 SQL 語句用於選擇所有的行？",
                    "SELECT *", "SELECT ALL", "SELECT ROWS", "SELECT DATA", "A"),
            new Question("請問哪個 CSS 屬性可用於設置文字的顏色？",
                    "text-align", "font-size", "color", "padding", "C"),
            new Question("請問在計算機科學中，「XML」代表什麼？",
                    "Extensible Markup Language", "Extensible Modeling Language",
                    "Extended Markup Language", "Extended Modeling Language", "A"));

    public Question question(int index) {
        return questions.get(index);
    }

    public String render(int index) {
        return questions.get(index).render(index);
    }

    public int size() {
        return questions.size();
    }
}
